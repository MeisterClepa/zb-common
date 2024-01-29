$reactions.answer = _.wrap($reactions.answer, function(realAnswer, params) {
    var ctx = $jsapi.context();

    var newParams = _.isObject(params) ? copyObject(params) : {value: params};
    newParams.value = resolveInlineDictionary(newParams.value);

    if (ctx.currentState.indexOf("CatchAll") === -1) {
        ctx.session.lastAnswerState = ctx.currentState;
    }

    realAnswer(newParams);
});
