require: text/text.sc
require: number/number.sc
require: floatNumber/floatNumber.sc
require: dateTime/dateTime.sc

require: address/address.sc
require: phoneNumber/phoneNumber.sc
require: music/musicGenre.sc
require: where/where.sc

require: city/cities-small.csv
    name = Cities
    var = $Cities
#TODO: pass dictionary name through $injector
require: city/city.sc

require: language/language.sc

require: color/color.sc

require: currency/currency.sc

require: car/carBrand.sc
require: car/carBrandEn.sc
require: car/carModel.sc
require: car/carModelEn.sc

require: metro/metro.sc

require: newSessionOnStart/newSession.sc
    injector = {
        newSessionStartState: "/NewSessionWelcome",
        newSessionTimeout: 10
        }

require: catchAll/catchAll.sc
    injector = {withOperator: true, CheckSameAnswer: true}


# require: offtopic/offtopic.sc
require: newOfftopic/newOfftopic.sc
require: patterns.sc
require: geocoder/geocoder.js
require: banks/banks.sc
require: reset/reset.sc

init:
    bind("preMatch", function($context) {
        if ($context.request.query === "/start") {
            $context.temp.targetState = "/Start";
        }
    });

    bind("onScriptError", function($context) {
        log("ERROR! " + $context.exception.message);
        if ($context.exception.message.endsWith("Date not valid")) {
            $reactions.answer("ERROR! Date not valid");
        }
    });

theme: /

    state: Agree
        q: $agree
        a: Вы согласились.

    state: PhoneNumber
        q: * $mobilePhoneNumber *
        a: phoneNumber: {{ $parseTree._mobilePhoneNumber}}

    state: NumberPattern
        q: * $Number *
        a: number: {{ $parseTree._Number }}

    state: NumberSequence
        q: number $Number $weight<1.05>
        q: number $Number [$Number]
        script:
            $temp.numbers = _.pluck($parseTree.Number, "value").join(", ")
        a: numbers: {{ $temp.numbers }}

    state: FloatNumberPattern
        q!: * [$Minus] $FloatNumber *
        a: floatNumber: {{$parseTree._FloatNumber}}

    state: DateTimePattern
        q: * $DateTime *
        a: dateTime: {{ toPrettyString( $parseTree._DateTime ) }}

    state: CityPattern
        q: * $City *
        if: $parseTree._City
            a: city: {{$parseTree._City.name}}
        else:
            a: 111

    state: EmailPattern
        q: * $email *
        a: email: {{$parseTree._email}}

    state: LanguagePattern
        q: * $Language *
        a: language: {{toPrettyString($parseTree._Language)}}

    state: ColorPattern
        q: $Color
        a: color: {{ toPrettyString($parseTree._Color) }}

    state: CurrencyPattern
        q: * ($Currency) *
        a: currency: {{ toPrettyString($parseTree._Currency.name) }}

    state: CarBrandPattern
        q: * $CarBrand *
        a: марка машины: {{ toPrettyString($parseTree._CarBrand.name) }}

    state: CarModelPattern
        q: * $CarModel *
        a: модель машины: {{ toPrettyString($parseTree._CarModel.name) }}

    state: AmbiguousCurrencyPattern
        q: * $ambiguousCurrency *
        a: ambiguousCurrency: {{ toPrettyString($parseTree._ambiguousCurrency.name) }}

    state: AgreePattern
        q: тест паттерна agree
        a: Можно отправить данные в техподдержку. Давай так и сделаем?

        state: Agree
            q: $agree
            a: Отлично! Я все отправил.

        state: CatchAll
            q: *
            a: Тогда лучше вернемся в Главное меню.

    state: NewSessionWelcome
        q: тест на создание новой сессии || toState = /Start
        a: welcome

        state: ContextPattern
            q: context pattern
            a: context

    state: TestNumberToString
        q: test numberToString
        a: Введите число

        state: GetNumeral
            q: * [$Minus] $Number *
            script:
                try {
                    $reactions.answer(numberToString($parseTree._Number));
                } catch(e) {
                    $reactions.answer(e.message);
                }

                try {
                    $reactions.answer(numberToString($parseTree._Number, "ordinal"));
                } catch(e) {
                    $reactions.answer(e.message);
                }

    state: MusicGenrePattern
        q: $MusicGenre
        a: Genre: {{$parseTree._MusicGenre}}

    state: FamilyPattern
        q: $relations
        a: Relation: {{$parseTree._relations}}

    state: TestGeocoder
        q: test geocoder
        a: Введите адрес

        state: GetAddress
            q: * $Address *
            script:
                log("Address    " + formatAddress($parseTree._Address));
                $temp.yandexCoordinates = getCoordinates(formatAddress($parseTree._Address), "yandex");
                $temp.googleCoordinates = getCoordinates(formatAddress($parseTree._Address), "google");
            a: Координаты яндекса: {{ toPrettyString($temp.yandexCoordinates) }}
            a: Координаты гугла: {{ toPrettyString($temp.googleCoordinates) }}

    state: DatePeriod
        q!: * $DatePeriod [past:past/future:future/futurepast:futurepast/pastfuture:pastfuture] *
        a: DatePeriod: {{ toPrettyString( $parseTree._DatePeriod ) }}
        script:
            if ($parseTree._Root == "past" || $parseTree._Root == "future") {
                $session.mode1 = $parseTree._Root;
                $session.mode2 = $parseTree._Root;
            } else if ($parseTree._Root == "futurepast") {
                $session.mode1 = "future";
                $session.mode2 = "past";
            } else if ($parseTree._Root == "pastfuture") {
                $session.mode1 = "past";
                $session.mode2 = "future";
            } else {
                $session.mode1 = "current";
                $session.mode2 = "current";
            }
            $temp.startDate = dateToString($parseTree._DatePeriod.startDate);
            $temp.endDate = dateToString($parseTree._DatePeriod.endDate);
        a: Период {{ checkPeriod($temp.startDate, $temp.endDate, $session.mode1, $session.mode2) }}
        a: Период в прошлом {{ checkPeriodInPast($temp.startDate, $temp.endDate, true, $session.mode1, $session.mode2) }}
        a: Период в будущем {{ checkPeriodInFuture($temp.startDate, $temp.endDate, true, $session.mode1, $session.mode2) }}
        a: Период строго в прошлом {{ checkPeriodInPast($temp.startDate, $temp.endDate, false, $session.mode1, $session.mode2) }}
        a: Период строго в будущем {{ checkPeriodInFuture($temp.startDate, $temp.endDate, false, $session.mode1, $session.mode2) }}
        a: Период в процессе {{ checkPeriodInProgress($temp.startDate, $temp.endDate, true, $session.mode1, $session.mode2) }}
        a: Период строго в процессе {{ checkPeriodInProgress($temp.startDate, $temp.endDate, false, $session.mode1, $session.mode2) }}

    state: Дата || modal = true
        q!: * Дата:current [past:past/future:future] *
        script:
            $session.mode = $parseTree._Root || "current";
        a: Введите дату

        state: Дата
            q: * $DateAbsolute * || fromState = .., onlyThisState = true
            a: Дата строго в прошлом {{ checkDateInPast(dateToString($parseTree._DateAbsolute), false, $session.mode) }}
            a: Дата строго в будущем {{ checkDateInFuture(dateToString($parseTree._DateAbsolute), false, $session.mode) }}
            a: Дата в прошлом {{ checkDateInPast(dateToString($parseTree._DateAbsolute), true, $session.mode) }}
            a: Дата в будущем {{ checkDateInFuture(dateToString($parseTree._DateAbsolute), true, $session.mode) }}

    state: BankTest
        state: Bank
            q!: * $Bank *
            a: Вы назвали банк: "{{$parseTree.Bank[0].value.name}}"

        state: 2Banks
            q!: * $Bank [и] $Bank *
            a: Вы назвали два банка: "{{$parseTree.Bank[0].value.name}}" и "{{$parseTree.Bank[1].value.name}}"

    state: Metro
        q!: * $Metro *
        a: Вы назвали метро: "{{$parseTree.Metro[0].value.name}}"

    state: ObsceneWord || modal = true
        q!: obsceneWord
        a: Назовите слово.

        state: Obscene
            q: * $obsceneWord *
            a: Это матерное слово.
            go: ..

        state: NotObscene
            q: *
            a: Это обычное слово.
            go: ..

    state: Region
        q!: * $Region *
        a: Вы назвали регион: {{$parseTree.Region[0].value.name}}. Наверное, вы из {{$parseTree.Region[0].value.genName}}? Вы сейчас находитесь в {{$parseTree.Region[0].value.locName}}? Или, может быть, вы хотите отправиться в {{$parseTree.Region[0].value.accName}}? Я бы с удовольствием поездил по {{$parseTree.Region[0].value.datName}}. А вы знаете, какие субъекты граничат с {{$parseTree.Region[0].value.insName}}?
