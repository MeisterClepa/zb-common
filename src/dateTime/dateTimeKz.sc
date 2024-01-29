require: ../number/numberKz.sc
require: moment.min.js
require: moment-business.js
require: dateTimeConverter.js
require: dateTime.js


patterns:

    $Season = ((~зима/~зимний/қыстық/қысқы/қыста/қыс ай*): 0|(~весна/~весенний/көктем ай*/көктемде/көктемдік/көктемгі): 1|(~лето/~летний/жаз ай*/жазда/жазғы/жаздық): 2|(~осень/осенний/күзде/күзгі/күздік/күз ай*): 3)

    $Weekend = (выходн*|уикенд*|викенд*|уикэнд*|викэнд*|демалыста|демалыс күн*)

    $TimeHhMmSs = $regexp<((0)?\d|1\d|2(0|1|2|3|4))(:|\.|-)((0|1|2|3|4|5)\d)((:|\.|-)((0|1|2|3|4|5)\d))?> ||  converter = $converters.timeConverterHhMmSs

    $TimeHhMmWithoutSeparator = $regexp<((0)?\d|1\d|2(0|1|2|3|4))(0|1|2|3|4|5)\d> ||  converter = $converters.timeConverterHhMmWithoutSeparator

    $TimeHourDigit = $regexp<((0)?\d|1\d|2(0|1|2|3|4))((-?|\s?)(де|ға|ге))?> ||  converter = $converters.numberConverterDigit

    $NumberTwoDigitTime = (
        $NumberTwoDigit | 
        (двадцат*|twenty|жиырма*):20 |
        (двадцат* ~один|twenty one|жиырма бір* ~один|twenty one):21 |
        (двадцат* ~два|twenty two|жиырма екі*|twenty two):22 |
        (двадцат* ~три|twenty three|жиырма үш*|twenty three):23) || converter = function(pt) {return pt.NumberTwoDigit ? pt.NumberTwoDigit[0].value : parseInt(pt.value);};

    $NumberOneDigitTime = (
        (~бір|біреу*|one):1 |
        (~екі|екеу*|two|[a] couple of):2 |
        (~үш|үшеу*|three):3 |
        (~төрт|төртеу|*four):4 |
        (~бес|бесеу*|five):5 |
        (~алты|алтау*|алтыда|six):6 |
        (~жеті|жетеу*|seven):7 |
        (~сегіз|eight*):8 |
        (~тоғыз|nine):9 )
            || converter = $converters.numberConverterValue

    $TimeHourNumber = ( $NumberDozen [$NumberOrdinalDigit | $NumberOneDigit] | $NumberOrdinalDigit | $NumberOneDigit | $NumberTwoDigitTime | $TimeHourDigit | $NumberOneDigitTime) ||  converter = $converters.numberConverterSum;

    $TimeMinuteDigit = $regexp<(0|1|2|3|4|5)?\d((-?|\s?)(де|ға|ге))?> ||  converter = $converters.numberConverterDigit

    $TimeMinuteNumber = ( $NumberDozen [$TimeMinuteDigit|$NumberOneDigit] | $TimeMinuteDigit | $NumberTwoDigit | (ноль|нөл*) [$NumberOneDigit] | $NumberOneDigitTime) ||  converter = $converters.numberConverterSum

    $DateDayDigit = $regexp<\d{1,2}(-(е|го|его|ого|інші|ыншы|і|сі|сы|ы))?> ||  converter = $converters.numberConverterDigit

    $DateDayAndMonthDigit = $regexp<(0?[1-9]|[1-2][0-9]|3[0-1])((\.|\/|,|\\)(1[0-2]|0?[1-9]))((\.|\/|,|\\)((1|2)?\d?\d\d))?>[г] || converter = $converters.timeConverterDateMonth

    $DateDayNumber = ( $NumberDozen [$NumberOrdinalDigit | $NumberNumeric] | ($NumberOrdinalDigit | $NumberNumeric) | $DateDayDigit | $DateDayOrderNumber) ||  converter = $converters.numberConverterSum

    $DateDayOrderNumber = (
        (в [самом] начале|в [самых] первых числах|басында|(бастапқы|бірінші|алғашқы) күн*):1 |
        (десятое|десятого|оныншы|оны):10 | 
        (одиннадцатое|одиннадцатого|он бірінші|он бірі):11 | 
        (двенадцатое|двенадцатого|он екінші|он екісі):12 | 
        (тринадцатое|тринадцатого|он үшінші|он үші):13 | 
        (четырнадцатое|четырнадцатого|он төртінші|он төрті):14 |
        (в середине|ортасында|орталарына|ортасына|жартысында|жартыларына):15 | 
        (пятнадцатое|пятнадцатого|он бесінші|он бесі):15 | 
        (шестнадцатое|шестнадцатого|он алтыншы|он алтысы):16 | 
        (семнадцатое|семнадцатого|он жетінші|он жетісі):17 | 
        (восемнадцатое|восемнадцатого|он сегізінші|он сегізі):18 | 
        (девятнадцатое|девятнадцатого|он тоғызыншы|он тоғызы):19 |
        (двадцатое|двадцатого|жиырмасыншы|жиырмасы):20 |
        (тридцатое|тридцатого|отызыншы|отызы):30 |
        (в конце|соңына|соңдарына|соңында|аяғында|аяғына):28 ) ||  converter = $converters.numberConverterValue

    $DateDayOrderOneDigitKz = (
        (бірі|бірінші):1 |
        (екісі|екінші):2 |
        (үші|үшінші):3 |
        (төртінші|төрті):4 |
        (бесі|бесінші):5 |
        (алтысы|алтыншы):6 |
        (жетісі|жетінші):7 |
        (сегізі|сегізінші):8 |
        (тоғызы|тоғызыншы):9 ) || converter = $converters.numberConverterValue

    $TimeHhMm = $TimeHourNumber $TimeMinuteNumber ||  converter = $converters.timeConverterHhMm

    $TimeHoursModifier = (
        (утра|утро|утром|утречком|ночи|ночью|таңертең|азанда|ертеңгісін|таңғы*|таң атқанда|таң атқан (кезде|уақытта)): am |
        (дня|днем|вечера|вечер*|пополудни|после полудня|күндіз|кешке|кешкі|күн|күн ортасын*|талтүс*|түске (қарай|жақын)|түсте|түстен (кейін|шейін)|тәулік|түнгі): pm |
        (ночи|ночью|түнде|түнге (қарай|жақын)|түннің): am ) ||  converter = $converters.propagateConverter

    # TODO : move утро/вечер/день into a separate pattern
    # TODO : add period modifier for час дня/час ночи

# maybe it's better to introduce special fields for morning and evening hours
    $TimeHoursSpecial = (
        (час дня|тәулік сағат*): 13 |
        (час ночи|түнгі сағат): 1 |
        (час|сағат): 1|
        (в час|сағатына): 1|
        часа: 1|
        (түскі|күндізгі|обед*) (бір|бірде|бірлерге|бірлерде|бірге): 13 |
        түн* (бір|бірде|бірлерге|бірге|бірлерде): 1 |
        ([с|на] утр*|поутру): 8 |
        [с|на] (вечер*): 18 |
        [с|на] ночь*: 0 |
        (ноч*|полночь|полночи|полуноч*): 0 |
        (түн ортасы*): 0 |
        (полдень|полудн*|түс|түске|түсте|түстен|түстерде|түстерге|талтүс*|он (екіде|екіге|екілерде|екілерге)): 12 |
        (таңертең*/ертеңгісін/азанда/таңғы уақыт*/таңға қарай):8 |
        (бірінші жартысында|таңғы|таңға):12 |
        (обед*/түске/түскі [асқа]/талтүс*) (дейін/үзіліс*/пауза*):13 |
        (күндіз*/күнгі уақыт*):12 |
        [пока $weight<-0.3>] ([в] обед/[во] время обеда/[в] {~обеденный (~время/~перерыв)}/{когда будет обед}):13 |
        ((обед*/түстен/түскі [астан|үзілістен|пауза]/талтүс*) (кейін/соң)|(түс*/талтүс*/үзіліс*/пауза*/обед) (кейін/соң)):15 |
        (екінші жартысында):18 |
        (кешке/кешкі уақыт*/жұмыс* (кейін/соң/біткен*)):18 |
        (түнде/түнге/түнгі уақыт*):21
        ) ||  converter = $converters.numberConverterValue

    $NumberNumericTime = ($NumberOneDigit | $NumberTwoDigitTime) || converter = $converters.propagateConverter;

    $TimeHoursNumeric = (
        $TimeHoursModifier [в|на] ($NumberNumericTime::Number|$TimeHourDigit::Number) [час*] |
        ($NumberNumericTime::Number|$TimeHourDigit::Number) ([час*] $TimeHoursModifier| (час|часа|часов|часиков) [$TimeHoursModifier]) |
        $TimeHoursSpecial|
        {$TimeHoursModifier ($NumberNumericTime::Number|$TimeHourDigit::Number) [сағат*]} |
        {($NumberNumericTime::Number|$TimeHourDigit::Number) ($TimeHoursModifier сағат*|(сағатта|сағаттан|сағат) [$TimeHoursModifier])}
        ) ||  converter = $converters.timeHoursConverter

    $TimeMinutesModifier = (
        без $Number [минут] |
        ($NumberNumeric::Number|$TimeMinuteNumber::Number|$Number) [минут*] (жоқ|жок) |
        $NumberNumeric::Number минут* [$TimeHoursNumeric] [сағат*] (жоқ|жок) |
        минут* (кейін/соң) |
        (кейін/соң) ($NumberNumeric::Number|$TimeMinuteNumber::Number|$Number) минут* |
        (без четверти|(он бес*/15*) минут* (кейін/соң/өткен*/болмас*)):15 |
        ([в] четверть|(қырық бесте/қырық бес [минут] (өткенде/кеткенде/болғанда))):45
        ) ||  converter = $converters.numberConverterSum

    $TimeRelativeHoursHalf = (
        $Number с половин* час* | 
        ((жарты/жарым) сағат* (кейін/соң/өткен*/болған*)/(жарымда*|жарымға)/жарым (болған*/өткен*/кеткен*)) | 
        $Number час* с половин* ) ||  converter = $converters.timeRelativeHoursConverter

    $TimeRelativeMinutesHalf = (
        $Number с половин* (минуты|минут|минуточек|минуточки) | 
        (жарым/жарты) минут* (кейін/соң/өткен*/кеткен*/болған*) | 
        $Number (минуты|минут|минуточек|минуточки) с половин* ) ||  converter = $converters.timeRelativeMinutesConverter

    $TimeRelativeHours = (
        ([один|одного] (час|часа|часик|часок)|бір сағат*) :1 |
        $Number час* | 
        ($NumberOneDigitTime | $NumberTwoDigitTime | $TimeHourDigit | $NumberThreeDigit) сағат* ) ||  converter = $converters.numberConverterSum

    $TimeRelativeMinutes = (
        ([одну|одна] минут*|(бір/1) минут* [кейін/соң*/өткен*/кеткен*]) :1 | 
        (две минут*|(бір-екі/ бір екі/бірнеше) минут* [кейін/соң*/өткен*/кеткен*]):2 |
        (четверт* часа|(он бес*/15*) минут* [кейін/соң*/өткен*/кеткен*]):15 |
        (полчас*|пол (часа|часика)|(жарты/жарым) сағат*/отыз минут*):30 | 
        (полтора час*|бір жарым сағат*):90 | 
        ($Number|$TimeMinuteDigit) (минут|минуты|минут|минуточек|минуточки|минуту|минуттан) | 
        ($TimeMinuteNumber (минуттар|минут|минутка|минуточек|минуттардан|минуту|минутку|минуттан))
        ) ||  converter = $converters.numberConverterSum

    $TimeRelativeSeconds = (
        ([одну] секунд*|(бір/1) секунд* (кейін/соң*/өткен*/кеткен*)) :1 |
        ((полминут*|пол минут*)|((жарты/жарым) минут/отыз секунд*)):30 |
        $Number секунд* (кейін/соң*/өткен*/кеткен*) |
        $Number секунд*
        ) ||  converter = $converters.numberConverterSum

    $TimeRelative = (
        через $TimeRelativeHours [и] [$TimeRelativeMinutes] |
        (через|на) $TimeRelativeMinutes |
        через ($TimeHourNumber::TimeRelativeHours|$TimeRelativeHours) $TimeMinuteNumber::TimeRelativeMinutes |
        час* через $Number::TimeRelativeHours |
        минут* через $Number::TimeRelativeMinutes |
        $TimeRelativeHours (кейін/соң*/өткен*/кеткен*) |
        $TimeRelativeMinutes (кейін/соң*/өткен*/кеткен*) |
        $TimeHourNumber::TimeRelativeHours (кейін/соң*/өткен*/кеткен*) $TimeMinuteNumber::TimeRelativeMinutes |
        $Number::TimeRelativeHours сағат* (кейін/соң*/өткен*/кеткен*) |
        $Number::TimeRelativeMinutes минут* (кейін/соң*/өткен*/кеткен*) |
        $TimeRelativeHoursHalf |
        $TimeRelativeMinutesHalf
        ) ||  converter = $converters.relativeDateTimeConverter

    $TimeHalfAnHourSpecial = (
        (полпервого | (пол|половин*) (первого|1)|(12*|он екі*) (жарымда*|жарымға)): 1 |
        (полвторого | (пол|половин*) (второго|2)|(бір*|1*) (жарымда*|жарымға)): 2 |
        (полтретьего | (пол|половин*) (третьего|3)|(екі*|2*) (жарымда*|жарымға)): 3 |
        (полчетвёртого | (пол|половин*) (четвёртого|4)|(үш*|3*)(жарымда*|жарымға)): 4 |
        (полпятого | (пол|половин*) (пятого|5)|(төрт*|4*) (жарымда*|жарымға)): 5 |
        (полшестого | (пол|половин*) (шестого|6)|(бес*|5*) (жарымда*|жарымға)): 6 |
        (полседьмого | (пол|половин*) (седьмого|7)|(алты*|6*)(жарымда*|жарымға)): 7 |
        (полвосьмого | (пол|половин*) (восьмого|8)|(жеті*|7*) (жарымда*|жарымға)): 8 |
        (полдевятого | (пол|половин*) (девятого|9)|(сегіз*|8*) (жарымда*|жарымға)): 9 |
        (полдесятого | (пол|половин*) (десятого|10)|(тоғыз*|9*) (жарымда*|жарымға)): 10 |
        (полодинадцатого | (пол|половин*) (одинадцатого|11)|(он*|10*) (жарымда*|жарымға)): 11 |
        (полдвенадцатого | (пол|половин*) (двенадцатого|12)|(он бір*|11*) (жарымда*|жарымға)): 12
        ) ||  converter = $converters.timeHalfAnHourConverter

    $TimeMinutesAndHours = [в] ($NumberOneDigit|$TimeMinuteNumber) (мин|минут*) ||  converter = $converters.propagateConverter

    $TimeMinutesNumeric = ([в] $TimeMinuteNumber| и $NumberOneDigit) [мин|минут*] ||  converter = $converters.propagateConverter

    $TimeSecondsNumeric = $Number [сек|секунд*] ||  converter = $converters.propagateConverter

    $TimeAbsolute = [в/на/после] (
        $TimeHhMmSs::Time |
        (на|в) $TimeHhMmWithoutSeparator::Time [$TimeHoursModifier] |
        [на|в] $TimeHhMmWithoutSeparator::Time $TimeHoursModifier |
        $TimeHalfAnHourSpecial::Time [$TimeHoursModifier] |
        [на|в] ($TimeHourNumber::TimeHoursNumeric|$TimeHourDigit::TimeHoursNumeric) [$TimeHoursModifier|(раз/раза/день/дня/дней) $weight<-1>] |
        [на|в] [сағат*] ($TimeHourNumber::TimeHoursNumeric|$TimeHourDigit::TimeHoursNumeric) [$TimeHoursModifier] |
        [на|в] [сағат*] $TimeHourNumber::TimeHoursNumeric [ноль|нөл|00] $TimeMinutesNumeric [$TimeHoursModifier] |
        $TimeHourNumber::TimeHoursNumeric сағатқа [$TimeHoursModifier] |
        $TimeMinutesModifier ($Number::TimeHoursNumeric | $TimeHoursNumeric) |
        [сағат*] $TimeHoursSpecial::TimeHoursNumeric [$TimeMinuteNumber::TimeMinutesNumeric] |
        [на|в] [сағат*] ($TimeHourNumber::TimeHoursNumeric [час*|часа|часов|сағат*] [и] $TimeMinutesNumeric) [$TimeHoursModifier] |
        [[на] $TimeHoursModifier] [на|в] [сағат*] ($TimeHourNumber::TimeHoursNumeric [час*|часа|часов|сағат*] [и] $TimeMinutesNumeric) |
        $TimeHoursNumeric |
        [на] $TimeMinutesAndHours ($Number::TimeHoursNumeric | $TimeHoursNumeric) |
        $TimeRelative |
        $TimeHhMmWithoutSeparator::Time [$TimeHoursModifier] (кейін|соң|өткен*)
        ) ||  converter = $converters.absoluteTimeConverter

    # Converts any date representation into absolute Date value
    $DateMonth = (
        (~январь|январ*|янв|january|jan|январ*|қаңтар*|янв|january):1 |
        (~февраль|феврал*|фев|february|feb|феврал*|ақпан*|фев|february):2 |
        (~март|март*|мар|march|mar|наурыз*|март*|мар|march):3 |
        (~апрель|апрел*|апр|april|apr|сәуір*|апрел*|апр|april):4 |
        (~май|май|мая|мае|may|мамыр*|май|мая|мае):5 |
        (~июнь|июнь|июня|июне|июн|june|jun|маусым*|июнь|июня|июне|июн|june):6 |
        (~июль|июль|июля|июле|июл|july|jul|шілде*|июль|июля|июле|июл|july):7 |
        (~август|август*|авг|august|aug|тамыз*|август*|авг|august):8 |
        (~сентябрь|сентябр*|сент|сен|september|sep|қыркүйек*|сентябр*|сент|сен|september):9 |
        (~октябрь|октябр*|окт|october|oct|қазан*|октябр*|окт|october):10 |
        (~ноябрь|ноябр*|ноя|нояб|november|nov|қараша*|ноябр*|ноя|нояб|november):11 |
        (~декабрь|декабр*|дек|december|dec|желоқсан*|декабр*|дек|december):12 ) ||  converter = $converters.numberConverterValue

    $DateWeekday = (
        (~понедельник/пн/понедельник*/дүйсенбі*):1 | (~вторник/вт/вторник*/сейсенбі*):2 | (~среда/ср/сәрсенбі*):3 | (~четверг/чт/бейсенбі*):4 | (~пятница/пт/жұма*):5 | (~суббота/субот*/сб/сенбі*):6 | (~воскресенье/~воскресение/вс/жексенбі*):7 
        ) ||  converter = $converters.numberConverterValue

    $DateFuturePastModifier = (
        (в (этом|эту|текущем)| этого|сего|текущего|ближайш*|жақында ):0 | 
        (следующ*) :1 |
        (следующ*/[на] следующ* недел*) :1 |
        прошл* :-1 |
        позапрошл* :-2 |
        через (~один|$Number):1 |
        (келесі*) :1 |
        (бір|$Number) * кейін:1 |
        (бір екі|бір-екі|бірнеше) * кейін:2
        ) ||  converter = $converters.numberConverterMultiply

    $DateRelativeDay = (
        [$DateFuturePastModifier] [недел*] [в|во|на] $DateWeekday |
        [в|во] $DateWeekday [$DateFuturePastModifier] |
        $DateFuturePastModifier (дня|дней|днем|день|сутки|суток|күні|күндер) |
        через [одни] сутки:1 |
        (сегодн*|бүгі*):0 |
        (вчера*|кеше):-1|
        (позавчера*|алдыңгүні|алдыңғы күні|бір күн бұрын):-2|
        (поза позавчера*|позапозавчера*|күн бұрын):-3|
        (завтра*|ертең*):1|
        (арғы күні|бүрсігүні|послезавтра*|после завтра*|күннен кейін):2 |
        (послепослезавтра*|после послезавтра*|после после завтра*):3 |
        [$Number] (дня|дней|день) [тому] назад:-1 |
        ((бір|1) күннен (кейін|соң)|(бір|1) күн* (өткеннен|кеткеннен)):1 |
        [$Number] күн бұрын:-1
        ) ||  converter = $converters.relativeDayConverter

    $DateRelativeMonth = (
        $DateFuturePastModifier (месяц*|айда*|айға) |
        [$Number] (месяц* назад|ай* бұрын):-1
        ) ||  converter = $converters.relativeMonthConverter

    $DateRelativeYear = (
        $DateFuturePastModifier (жыл*|год*) |
        [$Number] ((год*|лет) назад|жыл бұрын):-1
        ) ||  converter = $converters.relativeMonthConverter
        # relativeMonthConverter

    $DateRelative = (
        [$DateRelativeYear] [$DateRelativeMonth] $DateRelativeDay |
        [$DateRelativeYear] $DateRelativeMonth |
        $DateRelativeYear |
        $DateRelativeDay $DateRelativeMonth |
        $DateRelativeDay
        ) ||  converter = $converters.relativeDateConverter

    $DateYearTwoNumber = $regexp<\d\d> ||  converter = $converters.numberConverterDigit

    $DateYearNumber = $regexp<(1|2)\d\d\d(г|ж)?> ||  converter = $converters.numberConverterDigit

    $DateYearNumeric = $NumberThousands $NumberThreeDigit ||  converter = $converters.numberConverterSum

    $DateHoliday = (
        ({рождество* [христов*/православн*]}): 0 |
        ({католич* рождество*}): 1 |
        (новый* год*|жаңа жыл*): 2 |
        (~хеллоуин|~хелоуин|~хэллоуин|~хэлоуин): 3 |
        ((~день независимост*|тәуелсіздік күн*) [сша/соединен* штат* [америк*]]): 4 |
        (~день (свят* валентин*/[~все] влюблен*)): 5 |
        (~день свят* патрик*): 6 |
        (~день защитник* отечеств*|отан қорғау* күн*): 7 |
        ([международн*] женск* ~день|(әйелдер|қыздар) күн*): 8 |
        (~день ({весн* * [труд*]}/{труд* * [весн*]})): 9 |
        (~день ~победа|жеңіс күн*): 10 |
        (~день (~россия/рф/российск* федераци*)): 11 |
        (~день народн* единств*|бірлік күн*): 12 |
        ({~день татьян*}/[международн*] ~день студент*|студент күн*): 13 |
        (~день (смех*/дурак*)|күлкі күн*): 14 |
        (~день космонавт*|космонавт* күн*): 15 |
        (~день славянск* ({письм* * [культур*]}/{культур* * [письм*]})): 16 |
        ([международн*] ~день защит* дет*|бала* қорға* күні): 17 |
        (~день ~семья [{~любовь * [~верность]}/{~верность * [~любовь]}]): 18 |
        (~день [государствен*] флаг* [~россия/рф/российск* федераци*]): 19 |
        (~день ~знание|білім күн*): 20 |
        (~день учител*|мұғалім күн*): 21 |
        (~день конституци*|конституци* күн*): 22 |
        (~старый ~новый ~год|ескі жаңа жыл*): 23 |
        (~день благодарени*): 24 |
        (~день матер*|ана* күн*): 25 |
        ((~день (медик*/медицинск* работник*))|(медик*|медиц*) күн*): 26 |
        (~день (пудинг*/пуддинг*)): 27) || converter = function(pt) {return pt.value;};

    $DateAbsolute = (
        [$DateWeekday] ($DateDayNumber::DateDayNumeric|$DateDayOrderNumber::DateDayNumeric) [числа|күн*] {($DateMonth) [месяц*|ай*]} [ [в] $DateYearNumber::DateYearNumeric [год*|жыл*]] |
        {{($DateDayNumber::DateDayNumeric [числа|күн*] $DateMonth [месяц*|ай*]) [ $DateYearTwoNumber::DateYearNumeric (год*|жыл*) | ($DateDayDigit) ($DateMonth) | $DateYearNumber::DateYearNumeric [года|жыл*] | $DateYearNumeric [год*|жыл*] ]} [[в|во] $DateWeekday]} |
        [в/во] $DateWeekday $DateDayNumber::DateDayNumeric [числа|число|күн*] |
        [[в/во] $DateWeekday] $DateDayNumber::DateDayNumeric (числа|число|күн*) |
        [[в/во] $DateWeekday] $DateDayOrderNumber::DateDayNumeric [числа|число|күн*] |
        [это* [день]] $DateDayNumber::DateDayNumeric [число|күн*] $DateMonth [месяц*|ай*] [ $DateYearNumber::DateYearNumeric [года|жыл*] | $DateYearNumeric [год*|жыл*] | $DateYearTwoNumber::DateYearNumeric (год*|жыл*) ] | 
        $DateWeekday $DateDayNumber::DateDayNumeric |
        $DateRelativeYear $DateDayNumber::DateDayNumeric [числа|күн*] $DateMonth [месяц*|ай*] |
        $DateDayNumber::DateDayNumeric [числа|күн*] $DateMonth [месяц*|ай*] [в] $DateRelativeYear |
        $DateDayNumber::DateDayNumeric [числа|күн*] $DateRelativeMonth [месяц*|ай*] |
        [на] $DateRelative |
        ($DateYearNumber::DateYearNumeric | $Number::DateYearNumeric) (год*|жыл*) |
        $DateHoliday |
        $DateDayAndMonthDigit |
        в $DateMonth
        ) ||  converter = $converters.absoluteDateConverter

    $DateTimeSpecial = (
        $DateDayDigit::Day (в/на) $TimeAbsolute
        ) ||  converter = $converters.specialDateTimeConverter

    $DateWeekDaySpecial = [в|во] $DateWeekday ||  converter = $converters.weekDayConverter

    $DateTimeRelativeInterval = (
        (через|спустя) $DateTimeSimpleInterval :1 |
        $DateTimeSimpleInterval (кейін|соң|өткен ):1 |
        $DateTimeSimpleInterval (назад|бұрын):-1
        ) ||  converter = $converters.relativeDateTimeMultiplierConverter

    $DateTimeAbsolute = (
        $DateAbsolute |
        $TimeAbsolute |
        $TimeAbsolute [на|в|во] $DateAbsolute |
        $DateAbsolute [на|в|после|около] $TimeAbsolute [$DateWeekDaySpecial] |
        $DateTimeSpecial |
        {([на] $DateRelative|$DateTimeRelativeInterval) ([на|в|после|около] $TimeAbsolute) [$TimeHoursModifier]}) ||  converter = $converters.composeConverter

    $DateTimeRelativeYears = (
        ((один|через) (год|годик)|бір жылдан кейін):1 |
        $Number (года|годика|лет|жыл*|(жылдан|жылдардан) кейін) |
        (бір екі|бір-екі|бірнеше) жыл* кейін:2
        ) ||  converter = $converters.numberConverterSum

    $DateTimeRelativeMothes = (
        (один|через) месяц:1 |
        $Number (месяца|месяцев) | [через] полгода:6 |
        бір жарым жыл* (кейін|соң|өткен*|болған*):18 |
        бір ай* (кейін|соң|өткен*|болған*):1 |
        (бір айдан кейін|қыста|көктемде|жазда|күзде):1 |
        $Number ай* (кейін|соң|өткен*|болған*)| (бір екі|бір-екі|бірнеше) ай* (кейін|соң|өткен*|болған*):2 |
        (жарты жыл*|алты ай*) (кейін|соң|өткен*|болған*):6
        ) ||  converter = $converters.numberConverterSum

    $DateTimeRelativeWeeks = (
        $Number ~неделя :7 |
        ([одна|через] недел*):7 |
        {[через] $Number (~неделя|недельк*)} :7 |
        бір апта* (кейін|соң|өткен*|болған*):7 |
        $Number ай* (кейін|соң|өткен*|болған*):7 |
        (бір екі|бір-екі|бірнеше) апта* (кейін|соң|өткен*|болған*):14
        ) ||  converter = $converters.numberConverterMultiply

    $DateTimeRelativeSimpleDays = (
        (один/одни) (день/сутки):1 |
        через [один/одни] (день/сутки):1 |
        [один/одни] (день/сутки) назад:-1 | $Number суток | $Number (дня|дней)|
        бір (күн*|тәулік*) (кейін|соң|өткен*|болған*):1 |
        бір (күн*|тәулік*) бұрын:-1 |
        $Number (күн*|тәулік*) (кейін|соң|өткен*|болған*) | (бір екі|бір-екі|бірнеше) (күн|тәулік) (кейін|соң|өткен*|болған*):2)

    $DateTimeRelativeDays = ({$DateTimeRelativeSimpleDays [и] [$DateTimeRelativeWeeks]}|$DateTimeRelativeWeeks) ||  converter = $converters.numberConverterSum

    $DateTimeSimpleInterval = (
        $DateTimeRelativeYears [и] [$DateTimeRelativeMothes] [и] [$DateTimeRelativeDays] [и] [$TimeRelativeHours] [и] [$TimeRelativeMinutes] [и] [$TimeRelativeSeconds] |
        $DateTimeRelativeMothes [и] [$DateTimeRelativeDays] [и] [$TimeRelativeHours] [и] [$TimeRelativeMinutes] [и] [$TimeRelativeSeconds] |
        $DateTimeRelativeDays [и] [$TimeRelativeHours] [и] [$TimeRelativeMinutes] [и] [$TimeRelativeSeconds] |
        $TimeRelativeHours [и] [$TimeRelativeMinutes] [и] [$TimeRelativeSeconds] |
        $TimeRelativeMinutes [и] [$TimeRelativeSeconds] |
        $TimeRelativeSeconds |
        $TimeRelativeHoursHalf |
        $TimeRelativeMinutesHalf
        ) ||  converter = $converters.relativeDateTimeConverter

    $DateTimeRelativeSimple = (
        [через|спустя] $DateTimeSimpleInterval :1 |
        $DateTimeSimpleInterval (назад|бұрын):-1 |
        $DateTimeSimpleInterval (кейін|соң|өткен*):1
        ) ||  converter = $converters.relativeDateTimeMultiplierConverter

    $DateTimeNow = (сейчас|сегодня|этот день|бүгін|осы күні) ||  converter = $converters.nowDateTimeConverter

    $DateTimeRelative = ($DateTimeRelativeSimple|$DateTimeNow) ||  converter = $converters.propagateConverter

    $DateTime = ($DateTimeAbsolute|$DateTimeRelative) ||  converter = $converters.normalizeDateTimeConverter
    #$DateTime = ($DateTimeAbsolute|$DateTimeRelative) ||  converter = $converters.absoluteDateTimeConverter

    $DatePeriod = (
        [с] $DateAbsolute::startDate (по/до) $DateAbsolute::endDate |
        [с] $Number::startDate (по/до) $Number::endDate [$DateMonth] [$DateYearNumber] |
        $DateAbsolute::startDate - $DateAbsolute::endDate |
        $regexp<\d+((\.|\/)\d\d)?((\.|\/)(1|2)?\d?\d\d)?\s?(-|−|–|—)\s?\d+((\.|\/)\d\d)?((\.|\/)(1|2)?\d?\d\d)?> [$DateMonth] [$DateYearNumber] |
        $DateAbsolute::startDate баста* $DateAbsolute::endDate (дейін|шейін) |
        $Number::startDate баста* $Number::endDate [$DateMonth] [$DateYearNumber] (дейін|шейін)
        ) || converter = $converters.datePeriodConverter
