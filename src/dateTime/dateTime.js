/*
Checks that start date is before end date.
    Args:
        start (string): date in format DD.MM[.YY] (use dateToString() to convert from object {day: int, month: int, [year: int]})
        end (string): date in format DD.MM[.YY]
*/
function checkPeriod(start, end, noYearOptionStart, noYearOptionEnd) {
    noYearOptionStart = noYearOptionStart || "current";
    noYearOptionEnd = noYearOptionEnd || noYearOptionStart;
    var startDate = getFullDate(start, false, noYearOptionStart);
    var endDate = getFullDate(end, false, noYearOptionEnd);
    if (!moment(startDate, "DD.MM.YYYY").isValid() || !moment(endDate, "DD.MM.YYYY").isValid()) {
        throw new Error("Date not valid");
    }
    return moment(startDate, "DD.MM.YYYY").isSameOrBefore(moment(endDate, "DD.MM.YYYY"));
}

/*
Checks that date is in the past.
    Args:
        date (string): date in format DD.MM[.YY] (use dateToString() to convert from object {day: int, month: int, [year: int]})
        includeToday (boolean): if today should be included in past. false by default.
        noYearOption (string):  how to deal with dates without year. "current" by default:
            "current" - assign current year,
            "past" - assign this or previous year so that it is the closest to now in the past,
            "future" - assign this or next year so that it is the closest to now in future.
*/
function checkDateInPast(date, includeToday, noYearOption) {
    noYearOption = noYearOption || "current";
    includeToday = includeToday || false;
    var fullDate = getFullDate(date, includeToday, noYearOption);
    if (!moment(fullDate, "DD.MM.YYYY").isValid()) {
        throw new Error("Date not valid");
    }
    if (includeToday) {
        return moment(dayStart(currentDate())).isSameOrAfter(moment(fullDate, "DD.MM.YYYY"));
    }
    return moment(dayStart(currentDate())).isAfter(moment(fullDate, "DD.MM.YYYY"));
}

/*
Checks that date is in the future.
    Args:
        date (string): date in format DD.MM[.YY] (use dateToString() to convert from object {day: int, month: int, [year: int]})
        includeToday (boolean): if today should be included in future. false by default.
        noYearOption (string):  how to deal with dates without year. "current" by default:
            "current" - assign current year,
            "past" - assign this or previous year so that it is the closest to now in the past,
            "future" - assign this or next year so that it is the closest to now in future.
*/
function checkDateInFuture(date, includeToday, noYearOption) {
    noYearOption = noYearOption || "current";
    includeToday = includeToday || false;
    var fullDate = getFullDate(date, includeToday, noYearOption);
    if (!moment(fullDate, "DD.MM.YYYY").isValid()) {
        throw new Error("Date not valid");
    }
    if (includeToday) {
        return moment(dayStart(currentDate())).isSameOrBefore(moment(fullDate, "DD.MM.YYYY"));
    }
    return moment(dayStart(currentDate())).isBefore(moment(fullDate, "DD.MM.YYYY"));
}

/*
Checks that period is in the past.
    Args:
        start (string): date in format DD.MM[.YY] (use dateToString() to convert from object {day: int, month: int, [year: int]})
        end (string): date in format DD.MM[.YY]
        includeToday (boolean): if today should be included in past. false by default.
        noYearOptionStart (string):  how to deal with start date without year. "current" by default:
            "current" - assign current year,
            "past" - assign this or previous year so that it is the closest to now in the past,
            "future" - assign this or next year so that it is the closest to now in future.
        noYearOptionEnd (string):  how to deal with end date without year
*/
function checkPeriodInPast(start, end, includeToday, noYearOptionStart, noYearOptionEnd) {
    noYearOptionStart = noYearOptionStart || "current";
    noYearOptionEnd = noYearOptionEnd || noYearOptionStart;
    includeToday = includeToday || false;
    var startDate = getFullDate(start, includeToday, noYearOptionStart);
    var endDate = getFullDate(end, includeToday, noYearOptionEnd);
    if (!moment(startDate, "DD.MM.YYYY").isValid() || !moment(endDate, "DD.MM.YYYY").isValid()) {
        throw new Error("Date not valid");
    }
    if (includeToday) {
        return checkPeriod(startDate, endDate) && moment(dayStart(currentDate())).isSameOrAfter(moment(endDate, "DD.MM.YYYY"));
    }
    return checkPeriod(startDate, endDate) && moment(dayStart(currentDate())).isAfter(moment(endDate, "DD.MM.YYYY"));
}

/*
Checks that period is in the future.
    Args:
        start (string): date in format DD.MM[.YY] (use dateToString() to convert from object {day: int, month: int, [year: int]})
        end (string): date in format DD.MM[.YY]
        includeToday (boolean): if today should be included in future. false by default.
        noYearOptionStart (string):  how to deal with start date without year. "current" by default:
            "current" - assign current year,
            "past" - assign this or previous year so that it is the closest to now in the past,
            "future" - assign this or next year so that it is the closest to now in future.
        noYearOptionEnd (string):  how to deal with end date without year
*/
function checkPeriodInFuture(start, end, includeToday, noYearOptionStart, noYearOptionEnd) {
    noYearOptionStart = noYearOptionStart || "current";
    noYearOptionEnd = noYearOptionEnd || noYearOptionStart;
    includeToday = includeToday || false;
    var startDate = getFullDate(start, includeToday, noYearOptionStart);
    var endDate = getFullDate(end, includeToday, noYearOptionEnd);
    if (!moment(startDate, "DD.MM.YYYY").isValid() || !moment(endDate, "DD.MM.YYYY").isValid()) {
        throw new Error("Date not valid");
    }
    if (includeToday) {
        return checkPeriod(startDate, endDate) && moment(dayStart(currentDate())).isSameOrBefore(moment(startDate, "DD.MM.YYYY"));
    }
    return checkPeriod(startDate, endDate) && moment(dayStart(currentDate())).isBefore(moment(startDate, "DD.MM.YYYY"));
}

/*
Checks that period is in progress (start is in the past, end is in the future).
    Args:
        start (string): date in format DD.MM[.YY] (use dateToString() to convert from object {day: int, month: int, [year: int]})
        end (string): date in format DD.MM[.YY]
        includeToday (boolean): if today should be included in past and future. false by default.
        noYearOptionStart (string):  how to deal with start date without year. "current" by default:
            "current" - assign current year,
            "past" - assign this or previous year so that it is the closest to now in the past,
            "future" - assign this or next year so that it is the closest to now in future.
        noYearOptionEnd (string):  how to deal with end date without year
*/
function checkPeriodInProgress(start, end, includeToday, noYearOptionStart, noYearOptionEnd) {
    noYearOptionStart = noYearOptionStart || "current";
    noYearOptionEnd = noYearOptionEnd || noYearOptionStart;
    includeToday = includeToday || false;
    var startDate = getFullDate(start, includeToday, noYearOptionStart);
    var endDate = getFullDate(end, includeToday, noYearOptionEnd);
    if (!moment(startDate, "DD.MM.YYYY").isValid() || !moment(endDate, "DD.MM.YYYY").isValid()) {
        throw new Error("Date not valid");
    }
    if (includeToday) {
        var endIsAfter = moment(dayStart(currentDate())).isSameOrBefore(moment(endDate, "DD.MM.YYYY"));
        var startIsBefore = moment(dayStart(currentDate())).isSameOrAfter(moment(startDate, "DD.MM.YYYY"));
    } else {
        var endIsAfter = moment(dayStart(currentDate())).isBefore(moment(endDate, "DD.MM.YYYY"));
        var startIsBefore = moment(dayStart(currentDate())).isAfter(moment(startDate, "DD.MM.YYYY"));
    }
    return startIsBefore && endIsAfter;
}

// converts date object {day: int, [month: int, [year: int]]} into string day[.month[.year]]
// [] - optional part
function dateToString(dateObject) {
    if (typeof(dateObject) === "string") {
        return dateObject;
    }
    var dateString = String(dateObject.day);
    if (dateObject.month) {
        dateString += "." + dateObject.month;
        if (dateObject.year) {
            dateString += "." + dateObject.year;
        }
    }
    return dateString;
}

/*
Get year for a date without year.
Args:
    includeToday (boolean): if today should be included in past or future. false by default.
    noYearOption (string): defines how to assign year. "current" by default.
        "current" - assign current year,
        "past" - assign this or previous year so that it is the closest to now in the past,
        "future" - assign this or next year so that it is the closest to now in future.
*/
function getYear(date, includeToday, noYearOption) {
    includeToday = includeToday || false;
    noYearOption = noYearOption || "current";
    var thisYear = currentDate().year();
    if (noYearOption === "current") {
        return thisYear;
    }
    if (noYearOption === "past") {
        if (checkDateInPast(date + "." + thisYear, includeToday)) {
            return thisYear;
        }
        return thisYear - 1;
    }
    if (noYearOption === "future") {
        if (checkDateInFuture(date + "." + thisYear, includeToday)) {
            return thisYear;
        }
        return thisYear + 1;
    }
    throw new Error("noYearOption not valid: " + noYearOption);
}

/*
Get month and year for a day only date.
Args:
    includeToday (boolean): if today should be included in past or future. false by default.
    noMonthOption (string): defines how to assign month. "current" by default.
        "current" - assign current month,
        "past" - assign this or previous month so that it is the closest to now in the past,
        "future" - assign this or next month so that it is the closest to now in future.
*/
function getMonthAndYear(date, includeToday, noMonthOption) {
    includeToday = includeToday || false;
    noMonthOption = noMonthOption || "current";
    var newDate = moment({
        day: date,
        month: currentDate().month(),
        year: currentDate().year()
    });
    if (noMonthOption === "current") {
        return newDate.format("DD.MM.YYYY");
    }
    if (noMonthOption === "past") {
        if (checkDateInPast(newDate.format("DD.MM.YYYY"), includeToday)) {
            return newDate.format("DD.MM.YYYY");
        }
        return newDate.subtract(1, "months").format("DD.MM.YYYY");
    }
    if (noMonthOption === "future") {
        if (checkDateInFuture(newDate.format("DD.MM.YYYY"), includeToday)) {
            return newDate.format("DD.MM.YYYY");
        }
        return newDate.add(1, "months").format("DD.MM.YYYY");
    }
    throw new Error("noMonthOption not valid: " + noMonthOption);
}

/*
Check that the date is full (has day, month and year), and fill in missing slots if necessary.
Args:
    includeToday (boolean): if today should be included in past or future. false by default.
    noMonthOption (string): defines how to assign month. "current" by default.
        "current" - assign current month,
        "past" - assign this or previous month so that it is the closest to now in the past,
        "future" - assign this or next month so that it is the closest to now in future.
*/
function getFullDate(date, includeToday, noYearOption) {
    includeToday = includeToday || false;
    noYearOption = noYearOption || "current";
    if (date.split(".").length === 2) {
        return date + "." + getYear(date, includeToday, noYearOption);
    }
    if (date.split(".").length === 1) {
        return getMonthAndYear(date, includeToday, noYearOption);
    }
    return date;
}
