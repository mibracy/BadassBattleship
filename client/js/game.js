/**
 * Author: Tobi Schweiger <tschwei@siue.edu>
 *
 *     game.js takes care of rendering and interacting with the battleship grids
 *     and communicating with the server.
 */

const BASE_URL = "http://localhost:4567/api/";

// Lazily label the columns for the game.
const LABEL_COLUMNS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

const MAX_PLAYER_NAME_LENGTH = 20;

$(document).ready(function() {

    // Fade out the grids before we do anthing else.
    //$('.battle-grids').css('opacity', '0.3');

    // Create the two grids.

    //TODO: adopt OOP class pattern for grids and ships!
    createBattleGrid('#self', 10, 10);
    createBattleGrid('#opponent', 10, 10);

    // DRAGGABLE STUFF - WIP!
    /*  $( "#draggable5" ).draggable({ grid: [ 36, 36 ] });
    var pos = $('#self #cell-0-1').position();
     $(" #draggable5").css("top", pos.top);
     $(" #draggable5").css("left", pos.left);*/

    /* UI Setup */

    // Generate random player name
    $('#player-name').val('player' + $.now());

    // New Match Button
    $('#new-match').click(function() { newMatch() });

    // Join Match Button
    $('#join-match').click(function() { joinMatch($('#match-id').val()) });

    // Show warning and cleanup before players try to leave
    $(window).bind('beforeunload', function(){
        return 'Are you sure you want to leave? Your match will be aborted and you cannot return.';
    });

    //TODO: this is ugly!
    function createBattleGrid(table, width, height) {
        if(width > LABEL_COLUMNS.length) {
            console.error('Game grid cannot be larger than ' + LABEL_COLUMNS.length);
        }

        // First, draw the labels for the columns
        var label = '<tr><td class="coord"></td>';
        for(var l = 0; l < width; ++l) {
            label += '<td class="coord">' + LABEL_COLUMNS[l] + '</td>';
        }
        label += '</tr>';
        $(table).append(label);

        // Next, draw the actual grid and the row numbers
        for(var y = 1; y <= height; ++y){
            var row = '<tr><td class="coord">' + y + '</td>';
            for (var x = 0; x < width; ++x) {
                row += '<td id="cell-' + x + '-' + y + '"></td>';
            }
            row += '</tr>';

            $(table).append(row);
        }
    }

    function newMatch() {
        var name = validateName();
        if(name !== '') {
            send('match/new', {'name': name}, function (response) {
                $('#match-id').val(response.id).select();
                updateStatus(response);
            });
        }
    }

    function joinMatch(matchID) {
        var name = validateName();
        if(name !== '' && matchID !== '') {
            send('match/join', {'id': matchID, 'name': name}, function (response) {
                updateStatus(response);
            });
        }
    }

    function updateStatus(response) {
        $('#status').html('You are in a match. Status: <strong>' + response.status + '</strong> - Match ID: ' + response.id);
    }

    function errorStatus(error) {
        $('#status').html('An error occurred: ' + error);
    }

    function send(endpoint, data, callback) {
        $.ajax({
            method: 'GET',
            url: BASE_URL + endpoint,
            data: data,
            dataType: 'json',
            error: function(xhr, statusText, err) {
                var data = JSON.parse(xhr.responseText);
                console.log(xhr.responseText);
                errorStatus(data.error);
            }
        }).done(function( response ) {
            callback(response);
        });
    }

    function validateName() {
        var name = $('#player-name').val();
        if(name !== '' && name.length < MAX_PLAYER_NAME_LENGTH) {
            return name;
        } else {
            alert('Player name has to be set and cannot be more than ' + MAX_PLAYER_NAME_LENGTH + ' character.');
            return '';
        }
    }

});