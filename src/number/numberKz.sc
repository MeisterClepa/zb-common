# ---
# ДИСКЛЕЙМЕР, ПРОЧЕСТЬ ПЕРЕД ИСПОЛЬЗОВАНИЕМ
# Если этот файл планируется использовать в зависимостях проекта и совместно с этим файлом планируется использовать phoneNumber.sc и/или floatNumber.sc, 
# необходимо скопировать себе локально phoneNumber.sc и/или floatNumber.sc и поменять в них зависимость
# с "require: ../number/number.sc" на "require: ../number/numberKz.sc"
# ---

require: ../common.js
require: numberConverter.js

patterns:
    $NumberDigit = $regexp<[-−]?\d+>
            || converter = $converters.numberConverterDigit

    $NumberCommaSeparated = $regexp<[-−]?\d{3}\.\d{1,3}(\.\d{1,3})+>
            || converter = $converters.numberConverterCommaSeparatedDigit

    $NumberOrdinalDigit = $regexp<[-−]?\d?(1st|2nd|3rd|\dth)>
            || converter = $converters.numberConverterOrdinalDigit

    $NumberDotZero = $regexp<[-−]?[0-9]+(\.[0]{3,})+>
            || converter = $converters.numberConverterCommaSeparatedDigit

    $Minus = $regexp<(\-|\−|minus|минус)>

    $NumberZero = (нол*|нул*|zero|нөл*):0 || converter = $converters.numberConverterValue

    $NumberOneDigitNatural = (
        (~один|~одна|перв*|единиц*|единичк*|однерка|однёрка|one|first|~бір|біреу|бірінші):1 |
        (~два|дву*|две|втор*|пара|пару|двое|~двойка|~двоечка|two|[a] couple of|second|~екі|екеу|екінші|бір-екі):2 |
        (~три|трет*|трех|трое|three|~тройка|~троечка|~трешечка|third|~үш|үшеу|үшінші):3 |
        (~четыре|четверт*|четырех|четверо|~четверка|~четверочка|*four|fourth|~төрт|төртеу|төртінші):4 |
        (~пять|пята*|пято*|пятый|пятым*|пяти|пятер*|five|fifth|~бес|бесеу|бесінші):5 |
        (~шесть|шест*|шестым*|шести|шестер*|six|sixth|~алты|алтау|алтыншы):6 |
        (~семь|седьм*|семи|семер*|seven|seventh|~жеті|жетеу|жетінші):7 |
        (~восемь|восьм*|восем|eight*|~сегіз|сегізінші):8 |
        (~девять|~девятка|девят*|девятый|~девяточка|nine|ninth|~тоғыз|тоғызыншы):9 )
            || converter = $converters.numberConverterValue

    $NumberOneDigit = ( $NumberZero | $NumberOneDigitNatural) || converter = $converters.propagateConverter

    $NumberTwoDigit = (
        (десят*|ten|tenth|он):10 |
        (одиннадцат*|eleven*|он бір*):11 |
        (двенадцат*|twelve|twelfth|он екі*):12 |
        (тринадцат*|thirteen*|он үш*):13 |
        (четырнадцат*|fourteen*|он төрт*):14 |
        (пятнадцат*|fifteen*|он бес*):15 |
        (шестнадцат*|sixteen*|он алты*):16 |
        (семнадцат*|seventeen*|он жеті*):17 |
        (восемнадцат*|eighteen*|он сегіз*):18 |
        (девятнадцат*|nineteen*|он тоғыз*):19 )
            || converter = $converters.numberConverterValue

    $NumberNumeric = ($NumberOneDigit | $NumberTwoDigit )
            || converter = $converters.propagateConverter

    $NumberSimpleNoZero = ($NumberDigit | $NumberOrdinalDigit | $NumberOneDigitNatural ) || converter = $converters.numberConverterSum

    $NumberSimple = ($NumberDigit | $NumberOrdinalDigit | $NumberNumeric)
            || converter = $converters.numberConverterSum

    $NumberDozenWithDash = $regexp<(twenty|thirty|forty|fifty|sixty|seventy|eighty|ninety)-((one|two|three|four|five|six|seven|eight|nine)|(first|second|third|fourth|fifth|sixth|seventh|eighth|nineth))>
            || converter = $converters.numberConverterDozenDash

    $NumberDozen = (
        (двадцать|двадцат*|twenty|twentieth|жиырма*):20 |
        (тридцать|тридцат*|thirty|thirtieth|отыз*):30 |
        (сорок*|forty|fortieth|қырық*):40 |
        (пятьдесят|пятидесят*|fifty*|fiftieth|елу*):50 |
        (шестьдесят|шестидесят*|шисят|sixty|sixtieth|алпыс*):60 |
        (семьдесят|семидесят*|seventy|seventieth|жетпіс*):70 |
        (восемьдесят|восьмидесят*|eighty|eightieth|сексен*):80 |
        (девяносто|девяност*|ninety|ninetieth|тоқсан*):90 )
            || converter = $converters.numberConverterValue

    $NumberHundred = (
        (сто|ста|[~один] ~сотня|~соточка|жүз*/бір жүз*):100 |
        (двести|двухсот*|двухста|~два ~сотня|екі жүз*):200 |
        (триста|тристо|трехсот*|трёхста|трехста/~три ~сотня|үш жүз*):300 |
        (четырест*|четырехсот*|четырехста|четырёста/~четыре ~сотня|төрт жүз*):400 |
        (пятьсот|пятисот*|пятиста/~пять ~сотня|бес жүз*):500 |
        (шестьсот|шестисот*|шестиста/~шесть ~сотня|алты жүз*):600 |
        (семьсот|семисот*|семиста/~семь ~сотня|жеті жүз*):700 |
        (восемьсот|восьмисот*|восемьста/~восемь ~сотня|сегі* жүз*):800 |
        (девятьсот|девятисот*|девятиста/~девять ~сотня|тоғы* жүз*):900 )
            || converter = $converters.numberConverterValue

    $NumberHundredComplex =
        [$NumberDozen [$NumberSimpleNoZero::NumberSimple]|$NumberTwoDigit::NumberSimple] hundred*: 100
            || converter = $converters.numberConverterMultiply

    $NumberThreeDigit = (
        $NumberHundredComplex [and] [[$NumberDozen] [$NumberSimpleNoZero::NumberSimple]|$NumberDozenWithDash|$NumberTwoDigit::NumberSimple] |
        $NumberHundred [and] [$NumberTwoDigit|[$NumberDozen] [$NumberSimpleNoZero::NumberSimple]|$NumberDozenWithDash] |
        $NumberDozen [$NumberSimpleNoZero::NumberSimple] |
        $NumberDozenWithDash |
        $NumberSimple |
        $NumberTwoDigit::NumberSimple
        ) || converter = $converters.numberConverterSum

    $NumberThousands = ([$NumberThreeDigit] (тысяч*|тыщ*|тыс|thousand*|мың*) |
        $NumberThreeDigit (к|т)):1000
            || converter = $converters.numberConverterMultiply

    $NumberMillions = [$NumberThreeDigit] (миллион*|милион*|млн|лям*|million*|millon*):1000000
            || converter = $converters.numberConverterMultiply

    $NumberBillions = [$NumberThreeDigit] (миллиард*|лярд*|billion*|billones*):1000000000
            || converter = $converters.numberConverterMultiply

    $Number = [$Minus] (
        $NumberBillions [and|и|және] [$NumberMillions] [and|и|және] [$NumberThousands] [and|и|және] [$NumberThreeDigit] |
        $NumberMillions [and|и|және] [$NumberThousands] [and|и|және] [$NumberThreeDigit] |
        $NumberThousands [and|и|және] [$NumberThreeDigit] |
        $NumberThreeDigit |
        $NumberCommaSeparated |
        $NumberDotZero
        ) || converter = $converters.numberConverterSum

theme: /
    state: nlp || modal = true
        q: $Number  || fromState = .
