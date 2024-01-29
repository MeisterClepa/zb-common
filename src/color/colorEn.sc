require: color-en.csv
    name = Colors
    var = $Colors

init:
    if (!$global.$converters) {
        $global.$converters = {};
    }

    $global.$converters
        .colorConverter = function(parseTree) {
            var id = parseTree.Colors[0].value;
            return $Colors[id].value;
        };

patterns:
    $Color = $entity<Colors> || converter = $converters.colorConverter
