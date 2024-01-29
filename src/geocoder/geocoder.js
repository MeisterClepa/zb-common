function formatAddress(location) {
    var wordsToSkip = [".", "д", "дом", "из", "со", "на", "из", "перед", "живу", "живем", "живём", "адрес", "нахожусь", "находимся", "я", "мы", "мой", "наш", "наша", "наше", "моя", "г", "гор", "бул", "б-р", "ш", "пр-т", "пр", "просп", "пер", "переул", "наб", "набер"];
    var replacements = {
        "улица": ["у", "ул"],
        "с": ["стр", "строение"]
    };
    var address = "";
    var addressArray = location.split(/\s+/);
    for (var i = 0; i < addressArray.length; i++) {
        var w = addressArray[i];
        if (w.startsWith(".") || w.endsWith(".")) {
            w = w.replace(/\./g, "");
        }
        if (w.indexOf(".") !== -1) {
            w = w.replace(/\./g, " ");
        }
        var isReplaced = false;
        for (var replacement in replacements) {
            if (replacements[replacement].indexOf(w) !== -1) {
                isReplaced = true;
                address += " " + replacement;
                break;
            }
        }
        if (!isReplaced && wordsToSkip.indexOf(w) == -1) {
            address += " " + w;
        }
    }
    address = address.trim();

    var building = address.search(/с \d+$/);
    if (building !== -1) {
        address = address.substr(0, building) + address.substr(building).replace(/\s+/, " ");
    }
    address = address.trim();

    return address;
}

function parseCoordinatesFromYandexGeocoder(rawCoordinates) {
    if ((rawCoordinates) && (rawCoordinates.GeoObjectCollection)) {
        var geoObjectCollection = rawCoordinates.GeoObjectCollection;
        if (geoObjectCollection.featureMember.length > 0) {
            var geoObject = geoObjectCollection.featureMember[0];
            if ((geoObject.GeoObject) && (geoObject.GeoObject.Point) && (geoObject.GeoObject.Point.pos)) {
                var point = geoObject.GeoObject.Point.pos;
                var splittedPoint = point.split(" ");
                if (splittedPoint.length == 2) {
                    return splittedPoint;
                }
            }
        }
    }
    return [];
}

function getCoordinatesFromYandex(addressFormatted) {
    var apiKey = $jsapi.context().injector.geocoder.apiKeyYandex;
    var link = "https://geocode-maps.yandex.ru/1.x/?apikey=${key}&format=json&geocode=" + encodeURIComponent(addressFormatted);
    return $http.get(link, {
        timeout: 7000,
        query: {
            key: apiKey
        }
    }).then(parseHttpResponse).catch(httpError);
}

function getAddresFromCoordinatesYandex() {
    var $session = $jsapi.context().session;
    var apiKey = $jsapi.context().injector.geocoder.apiKeyYandex;

    var link = "https://geocode-maps.yandex.ru/1.x/?apikey=${key}&format=json&geocode=" + $session.lon + "," + $session.lat;
    return $http.get(link, {
        timeout: 7000,
        query: {
            key: apiKey
        }
    }).then(parseHttpResponse).catch(httpError);
}

function getCoordinatesFromGoogle(addressFormatted) {
    var apiKey = $jsapi.context().injector.geocoder.apiKeyGoogle;
    var link = "https://maps.googleapis.com/maps/api/geocode/json?key=${key}&address=" + encodeURIComponent(addressFormatted);
    return $http.get(link, {
        timeout: 10000,
        query: {
            key: apiKey
        }
    }).then(parseHttpResponse).catch(httpError);
}

function getCoordinates(location, geocoder) {
    var address = formatAddress(location);
    var coordinates;
    var latitude;
    var longitude;
    var res;

    if (geocoder && geocoder.toLowerCase() === "google") {
        res = getCoordinatesFromGoogle(address);
        if (res.isOk && res.data.results[0]) {
            latitude = res.data.results[0].geometry.location.lat;
            longitude = res.data.results[0].geometry.location.lng;
            coordinates = {lat: latitude, lon: longitude};
        } else {
            log("Failed to get coordinates from Google geocoder. Response: " + toPrettyString(res));
            coordinates = {};
        }
    } else {
        res = getCoordinatesFromYandex(address);
        var parsingResult = parseCoordinatesFromYandexGeocoder(res.data.response);
        if (parsingResult.length === 2) {
            longitude = parsingResult[0];
            latitude = parsingResult[1];
            coordinates = {lat: latitude, lon: longitude};
        } else {
            log("Failed to get coordinates from Yandex geocoder. Response: " + toPrettyString(res));
            coordinates = {};
        }
    }
    return coordinates;
}
