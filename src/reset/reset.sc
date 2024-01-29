theme: /

    state: Reset
        q!: $regex</?reset>
        script:
            $session = {};
            $client = {};
            $temp = {};
            $response = {};
            $temp.matcherResult = $nlp.match("/start", "/");
        a: Данные сброшены.
        if: $temp.matcherResult && $temp.matcherResult.targetState
            go!: {{$temp.matcherResult.targetState}}
        else:
            go: /
