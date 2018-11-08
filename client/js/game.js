/**
 * Author: Tobi Schweiger <tschwei@siue.edu>
 *
 *     game.js takes care of rendering and interacting with the battleship grids
 *     and communicating with the server.
 */

// Lazily label the columns for the game.
const LABEL_COLUMNS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

$(document).ready(function() {

    // Create the two grids.
    createBattleGrid('#self', 10, 10);
    createBattleGrid('#opponent', 10, 10);

    /**
     * Generate a "battle" grid table with proper headings and so on
     * @param table     selector for table element
     * @param width     width of grid
     * @param height    height of grid
     */
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

});