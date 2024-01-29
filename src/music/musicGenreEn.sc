require: musicGenres.csv
  name = MusicGenres
  var = $MusicGenres

init:
    if (!$global.$converters) {
        $global.$converters = {};
    }

    $global.$converters
        .musicGenresConverter = function(parseTree) {
            var id = parseTree.MusicGenres[0].value;
            return $MusicGenres[id].value.en.name;
        };

patterns:
    $MusicGenre = $entity<MusicGenres> || converter = $converters.musicGenresConverter;
