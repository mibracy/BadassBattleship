/**
 * Author: Tobi Schweiger <tschwei@siue.edu>
 *
 *     game.js takes care of rendering and interacting with the battleship grids
 *     and communicating with the server. This code could be a lot better.
 */

const BASE_URL = "/api/";

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

// --- GAME VARIABLES ---
var matchID;
var playerID;
var myTurn = false;

var sunk = 0;

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

    var prevOffset, curOffset;

    //TODO: BUG - keep dragging altough stopped: registers as not-stopped, i.e. one off!
    $('.ship').draggable({
        grid: [ CELL_SIZE, CELL_SIZE ],
        containment: '.actual-grid',
        cursor: 'move',
        drag: function(event, ui) {
            prevOffset = curOffset;
            curOffset = $.extend({},ui.offset);
        },
        stop: function(event, ui) {
            console.log("Cur | Prev | Actual");
            console.log(curOffset);
            console.log(prevOffset);
            console.log(ui.position);
            var x = Math.round(ui.position.left / CELL_SIZE);
            var y = Math.round(ui.position.top / CELL_SIZE);

            // Update the local array of ships
            var ship = ships.find(x => x.id === event.target.id);

            // ITS A DUPLICATE YOU IDIOT
            console.log("moving ship " + event.target.id + " to " + x + ", " + y);

            ship.position.x = x;
            ship.position.y = y;

            //console.log("json: " + JSON.stringify(ships));
        }
    }).droppable({
        // Prevent the ship from being placed too close to other ships
        greedy: false,
        over: function(e,ui) {
            ui.helper.offset(curOffset = prevOffset).trigger('mouseup');
        },
        tolerance: 'touch'
    });

    function createBattleGrid(table, width, height) {
        // Next, draw the actual grid and the row numbers
        for(var y = 0; y < height; ++y){
            var row = '<tr>';
            for (var x = 0; x < width; ++x) {
                row += '<td data-x="' + x + '" data-y="' + y + '"></td>';
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
                playerID = response.new_player_id;

                showCancelMatchbar();

                $('#current-match-id').val(matchID).select();

                start(matchID);
            });
        }
    }

    function joinMatch(id) {
        var name = validateName();
        if(name !== '' && id !== '') {
            send('match/join', {'id': id, 'name': name, 'ships': JSON.stringify(ships)}, function (response) {
                matchID = response.id;
                playerID = response.new_player_id;

                showCancelMatchbar();
                $('#current-match-id').hide();

                start(matchID);
            });
        }
    }

    function gameover(won) {
        alert("Game over! " + (won ? "You won!" : "You lost!"));
        updateStatus('Game over! <a href="index.html">New game?</a>');

        $('#opponent,#self').css('opacity', '0.15');
    }

    function start(matchID) {
        // Cannot drag anymore
        $('.ship').draggable( 'disable' );

        update();
        setInterval(update, REFRESH_RATE);
    }

    //TODO: make code better
    $('#opponent td').click(function(event) {
        var cell =  $(this);
        if(myTurn && !cell.hasClass('hit') && !cell.hasClass('miss')) {
            send('match/hit', {'id': matchID, 'player_id': playerID, 'x': cell.attr('data-x'), 'y': cell.attr('data-y')},
                function (response) {
                    // Check if this was a valid hit
                    //TODO: replace this with consts or something, it's ugly
                    if(response.status === 'HIT' || response.status === 'SUNK_SHIP' || response.status === 'GAME_OVER') {
                        cell.addClass('hit');
                    } else if(response.status === 'MISS') {
                        cell.addClass('miss');
                    }

                    if(response.status === 'SUNK_SHIP') {
                        sunk++;
                        $('#sunk').html(sunk);
                    } else if(response.status === 'GAME_OVER') {
                        $('#sunk').html('GAME OVER');
                        gameover(true);
                    }

                    update();
                }
            );
        }
    });

    function showCancelMatchbar() {
        $('#end-match-bar').show();
        $('#match-bar').hide();
    }

    function update() {
        // todo: clean this up
        send('match/status', {'id': matchID, 'player_id': playerID }, function (response) {
           updateStatus('Status: <strong>' + response.status + '</strong>');
           if(response.status === 'PLAYING') {
               myTurn = response.turn === playerID;
               updateStatus(myTurn ? 'Your turn: Try to hit the opponent\'s ships!' : 'Opponent\'s turn: Watch out!');

               $('#opponent').css('opacity', myTurn ? '1' : '0.25');

               if(response.recent_hit_position) {
                   //todo: fix this
                   var state = response.recent_hit_state;

                   $('#self td[data-x="' + response.recent_hit_position.x + '"][data-y="' + response.recent_hit_position.y + '"]')
                       .addClass(state === 'MISS' ? 'miss' : 'hit');

                   if(state === 'GAME_OVER') {
                       gameover(false);
                   }
               }
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