require: names-ru.csv
  name = NamesRu
  var = NamesRu

init:
    if (!$global.$converters) {
        $global.$converters = {};
    }
    $global.$converters
        .NamesRuConverter = function(parseTree) {
            var id = parseTree.NamesRu[0].value;
            return NamesRu[id].value;
        };

patterns:
    $Name = $entity<NamesRu> || converter = $converters.NamesRuConverter