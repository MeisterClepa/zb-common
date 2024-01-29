require: metro.csv
    name = MetroCsv
    var = $MetroCsv

init:
    if (!$global.$converters) {
        $global.$converters = {};
    }

    $global.$converters
        .metroConverter = function(parseTree) {
            var id = parseTree.MetroCsv[0].value;
            return $MetroCsv[id].value;
        };

patterns:
    $Metro = {[~станция/[~станция] метро] $entity<MetroCsv>} || converter = $converters.metroConverter;
