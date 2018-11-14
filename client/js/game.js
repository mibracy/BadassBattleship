/**
 * Author: Tobi Schweiger <tschwei@siue.edu>
 *
 *     game.js takes care of rendering and interacting with the battleship grids
 *     and communicating with the server.
 */

const BASE_URL = "http://localhost:4567/api/";

const MAX_PLAYER_NAME_LENGTH = 20;

const CELL_SIZE = 40; //if you modify this, it needs to be changed in the CSS

const REFRESH_RATE = 1000;

const ORIENTATION = { Horizontal: 0, Vertical: 1 };

const SHIPS = [
    {
        id: 'ship-carrier',
        size: 5,
        type: 'CARRIER',
        orientation: ORIENTATION.Horizontal,
        position: { x: 1, y: 0 },
    },
    {
        id: 'ship-battleship',
        size: 4,
        type: 'BATTLESHIP',
        orientation: ORIENTATION.Vertical,
        position: { x: 1, y: 2 }
    },
    {
        id: 'ship-cruiser',
        size: 3,
        type: 'CARRIER',
        orientation: ORIENTATION.Horizontal,
        position: { x: 4, y: 4 }
    },
    {
        id: 'ship-submarine',
        size: 3,
        type: 'CARRIER',
        orientation: ORIENTATION.Vertical,
        position: { x: 9, y: 5 }
    },
    {
        id: 'ship-submarine-second',
        size: 3,
        type: 'SUBMARINE',
        orientation: ORIENTATION.Vertical,
        position: { x: 6, y: 6 }
    },
    {
        id: 'ship-destroyer',
        size: 2,
        type: 'CARRIER',
        orientation: ORIENTATION.Horizontal,
        position: { x: 2, y: 8 }
    },
    {
        id: 'ship-destroyer-second',
        size: 2,
        type: 'CARRIER',
        orientation: ORIENTATION.Horizontal,
        position: { x: 7, y: 1 }
    }
];

var matchID;
var playerName;

$(document).ready(function() {

    createBattleGrid('#self', 10, 10);
    createBattleGrid('#opponent', 10, 10);

    spawnShips(SHIPS);

    /* UI Setup */

    $('#end-match-bar').hide();

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

    function spawnShips(ships) {
        ships.forEach(function(ship) {
            $('.actual-grid').append('<div id=\"' + ship.id + '\" class="ship"></div>');
            let shipDiv = $('#' + ship.id);

            if(ship.orientation === ORIENTATION.Horizontal) {
                shipDiv.css('width', ship.size * CELL_SIZE + 'px');
            } else {
                shipDiv.css('height', ship.size * CELL_SIZE + 'px');
            }

            shipDiv.css('left', ship.position.x * CELL_SIZE + 'px').css('top', ship.position.y * CELL_SIZE + 'px');
        });
    }

    var prevOffset, curOffset;

    $( '.ship' ).draggable({
        grid: [ CELL_SIZE, CELL_SIZE ],
        containment: '.actual-grid',
        cursor: 'move',
        drag: function(event, ui) {
            var x = ui.position.left / CELL_SIZE;
            var y = ui.position.top / CELL_SIZE;
            console.log('being dragged! ' + x + ' ' + y);
            prevOffset = curOffset;
            curOffset = $.extend({},ui.offset);
        }
    }).droppable({
        // Prevent the ship from being placed too close to other ships
        greedy: true,
        over: function(e,ui) {
            ui.helper.offset(curOffset= prevOffset).trigger("mouseup");
        },
        tolerance: "touch"
    }).dblclick(function() {
        console.log('db clic');
    });

    function createBattleGrid(table, width, height) {
        // Next, draw the actual grid and the row numbers
        for(var y = 0; y < height; ++y){
            var row = '<tr>';
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
                matchID = response.id;
                playerName = name;

                showCancelMatchbar();

                $('#current-match-id').val(matchID).select();

                update();
                setInterval(update, REFRESH_RATE);
            });
        }
    }

    function joinMatch(id) {
        var name = validateName();
        if(name !== '' && id !== '') {
            send('match/join', {'id': id, 'name': name}, function (response) {
                matchID = response.id;
                playerName = name;

                showCancelMatchbar();
                $('#current-match-id').hide();

                update();
                setInterval(update, REFRESH_RATE);
            });
        }
    }

    function showCancelMatchbar() {
        $('#end-match-bar').show();
        $('#match-bar').hide();
    }

    function update() {

        // Need to get info about match.
        send('match/status', {'id': matchID }, function (response) {
           updateStatus('Status: <strong>' + response.status + '</strong>');
           if(response.status === 'PLAYING') {
               updateStatus((response.playerTurn === playerName ? 'Your turn' : 'Opponent turn'));
           }
        });
    }

    function updateStatus(message) {
        $('#status').html(message);
    }

    function errorStatus(error) {
        updateStatus('An error occurred: ' + error);
    }

    //todo (tobi): specify method, make POST!
    function send(endpoint, data, callback) {
        $.ajax({
            method: 'GET',
            url: BASE_URL + endpoint,
            data: data,
            timeout: 2000,
            dataType: 'json',
            error: function(xhr, statusText, err) {
                var data = JSON.parse(xhr.responseText);
                errorStatus(data.error);
            },
            success: callback
        }).fail(function (jqXHR, textStatus, errorThrown) {
            $('#end-match-bar').hide();
            $('#match-bar').show();
            errorStatus(errorThrown);
        });
    }

    function validateName() {
        var name = $('#player-name').val();
        if(name !== '' && name.length < MAX_PLAYER_NAME_LENGTH) {
            return name;
        } else {
            errorStatus('Player name has to be set and cannot be more than ' + MAX_PLAYER_NAME_LENGTH + ' character.');
            return '';
        }
    }

});