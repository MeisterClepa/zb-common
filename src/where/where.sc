require: ../city/city.sc
require: ../city/cities-ru.csv
  name = Cities
  var = $Cities
require: whereConverter.js
require: countries-ru.csv
  name = Countries
  var = $Countries
require: geography-ru.csv
  name = Geography
  var = $Geography
require: regions.csv
# В справочнике нет городов федерального значения. Следует использовать сущность в связке с $City или добавлять их вручную.
  name = Regions
  var = $Regions

require: ../common.js
  
init:
    if (!$global.$converters) {
        $global.$converters = {};
    }

    bind("preProcess", function($context) {
      if ($context.parseTree && $context.parseTree.Where && $context.parseTree.Where[0].value.error && $context.parseTree.Where[0].value.error == "unknown location") {
        $context.temp.targetState = "/Errors/Unknown location";
      }
    });

patterns:
    $Where = ($City|$Capital|$Country2City) || converter = $converters.propagateConverter;
    $Capital = $entity<Geography> || converter = $converters.geographyConverter;
    $Country = $entity<Countries> || converter = $converters.countryConverter;
    $Country2City = $entity<Countries> || converter = $converters.country2CapitalConverter;
    $Region = $entity<Regions> || converter = $converters.regionsConverter;

theme: /Errors

    state: Unknown location || noContext = true
        a: Не могу найти координаты для страны {{$parseTree.Where[0].value.country}}. Обычно я могу найти координаты, если знаю название нужного города.