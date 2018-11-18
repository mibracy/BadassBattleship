/**
 * Author: Tobi Schweiger <tschwei@siue.edu>
 *
 *     game.js takes care of rendering and interacting with the battleship grids
 *     and communicating with the server. This code could be a lot better.
 */

const BASE_URL = "http://localhost:4567/api/";

const MAX_PLAYER_NAME_LENGTH = 20;

const CELL_SIZE = 40;

const REFRESH_RATE = 1000;

const ORIENTATION = { Horizontal: 0, Vertical: 1 };

var ships = [
    {
        id: 'ship-carrier',
        size: 5,
        orientation: ORIENTATION.Horizontal,
        position: { x: 0, y: 0 },
    },
    {
        id: 'ship-battleship',
        size: 4,
        orientation: ORIENTATION.Vertical,
        position: { x: 1, y: 2 }
    },
    {
        id: 'ship-cruiser',
        size: 3,
        orientation: ORIENTATION.Horizontal,
        position: { x: 4, y: 4 }
    },
    {
        id: 'ship-submarine',
        size: 3,
        orientation: ORIENTATION.Vertical,
        position: { x: 9, y: 5 }
    },
    {
        id: 'ship-submarine-second',
        size: 3,
        orientation: ORIENTATION.Vertical,
        position: { x: 6, y: 6 }
    },
    {
        id: 'ship-destroyer',
        size: 2,
        orientation: ORIENTATION.Horizontal,
        position: { x: 2, y: 8 }
    },
    {
        id: 'ship-destroyer-second',
        size: 2,
        orientation: ORIENTATION.Horizontal,
        position: { x: 7, y: 1 }
    }
];

var matchID;
var playerName;
var myTurn = false;

$(document).ready(function() {

    createBattleGrid('#self', 10, 10);
    createBattleGrid('#opponent', 10, 10);

    spawnShips();

    /* UI Setup */

    $('#opponent').css('opacity', '0.25');

    $('#end-match-bar').hide();

    // Generate random player name
    $('#player-name').val('player' + $.now());

    // New Match Button
    $('#new-match').click(function() { newMatch() });

    // Join Match Button
    $('#join-match').click(function() { joinMatch($('#match-id').val()) });

    function spawnShips() {
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

    //todo: clean up
    var prevOffset, curOffset;

    $('.ship').draggable({
        grid: [ CELL_SIZE, CELL_SIZE ],
        containment: '.actual-grid',
        cursor: 'move',
        drag: function(event, ui) {
            prevOffset = curOffset;
            curOffset = $.extend({},ui.offset);
        },
        stop: function(event, ui) {
            var x = ui.position.left / CELL_SIZE;
            var y = ui.position.top / CELL_SIZE;

            // Update the local array of ships
            var ship = ships.find(x => x.id === event.target.id);

            ship.position.x = x;
            ship.position.y = y;
        }
    }).droppable({
        // Prevent the ship from being placed too close to other ships
        greedy: false,
        over: function(e,ui) {
            ui.helper.offset(curOffset = prevOffset).trigger('mouseup');
        },
        tolerance: 'touch'
    }).dblclick(function(event) {
        //console.log(event.target);
        //$(event.target).css('transform', 'rotate(90deg)');
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
            send('match/new', {'name': name, 'ships': JSON.stringify(ships)}, function (response) {
                console.log(JSON.stringify(ships));

                matchID = response.id;
                playerName = name;

                showCancelMatchbar();

                $('#current-match-id').val(matchID).select();

                start(matchID);
            });
        }
    }

    $('#opponent td').click(function(event) {
        if(myTurn) {
            console.log('hit cell ' + event.target.id);
        }
    });

    function joinMatch(id) {
        var name = validateName();
        if(name !== '' && id !== '') {
            send('match/join', {'id': id, 'name': name, 'ships': JSON.stringify(ships)}, function (response) {
                matchID = response.id;
                playerName = name;

                showCancelMatchbar();
                $('#current-match-id').hide();

                start(matchID);
            });
        }
    }

    function start(matchID) {
        // Cannot drag anymore
        $('.ship').draggable( 'disable' );

        update();
        setInterval(update, REFRESH_RATE);
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
               myTurn = response.playerTurn === playerName;
               updateStatus(myTurn ? 'Your turn: Try to hit the opponent\'s ships!' : 'Opponent turn: Watch out!');

               $('#opponent').css('opacity', myTurn ? '1' : '0.25');
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