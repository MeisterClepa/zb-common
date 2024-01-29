require: ../namesEn/names-En.csv
  name = Names
  var = $Names

init:
    if (!$global.$converters) {
        $global.$converters = {};
    }
    $global.$converters
        .NamesConverter = function(parseTree) {
            var id = parseTree.Names[0].value;
            return $Names[id].value;
        };

patterns:
    $Name = $entity<Names> || converter = $converters.NamesConverter