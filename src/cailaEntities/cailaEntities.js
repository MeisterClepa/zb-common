// в chatbot.yaml дописать injector
/*
injector:
    cailaEntities:
        # идентификаторы сущности
        commonId: здесь уникальная строка
        commonEntityName: здесь другая уникальная строка
*/

// в основном используются
// cailaEntities.add, чтобы записать данные
// и
// cailaEntities.get, чтобы получить данные

// ruleUniqueId - это ваш кастомный уникальный "ключ",
// по которому будет производиться поиск и запись сущностей
// напр., в кейсе бота секретаря это был номер клиента

var commonId = $injector.cailaEntities.commonId;
var commonEntityName = $injector.cailaEntities.commonEntityName;

var cailaEntities = {
    getPreparedRecord: function(ruleUniqueId, data, recordId) {
        var stringData = JSON.stringify(data);
        var record = {
            "type": "pattern",
            "rule": [ruleUniqueId],
            "value": stringData
        };

        if (recordId) {
            record.id = recordId;
        } else {
            record = [record];
        }

        return record;
    },
    add: function(ruleUniqueId, data) {
        var previousRecord = cailaEntities.get(ruleUniqueId);
        if (previousRecord) {
           cailaEntities.update(ruleUniqueId, data, previousRecord.recordId);
           return;
        }

        var record = cailaEntities.getPreparedRecord(ruleUniqueId, data);

        $caila.addClientEntityRecords(
            commonEntityName,
            record,
            commonId
        );
    },
    getAllRecords: function() {
        return $caila.getClientEntityRecords(commonEntityName, commonId);
    },
    update: function(ruleUniqueId, data, recordId) {
        recordId = recordId || cailaEntities.get(ruleUniqueId).recordId;

        var updatedRecord = cailaEntities.getPreparedRecord(ruleUniqueId, data, recordId);

        $caila.setClientEntityRecord(commonEntityName, updatedRecord, commonId);
    },
    get: function(ruleUniqueId) {
        var entityRecords = cailaEntities.getAllRecords();
        var data = _.find(entityRecords, function(record) {
            return record.rule && String(record.rule[0]) === String(ruleUniqueId);
        });

        if (!data) {
            return null;
        }

        var id = data.id;
        var value = JSON.parse(data.value);

        value.recordId = id;

        return value;
    },
    deleteById: function(id) {
        // type of id = int or arr
        // param id is record id not ruleUniqueId
        if (typeof id !== "object") {
            id = [id];
        }
        $caila.deleteClientEntityRecords(commonEntityName, id, commonId);
    },
    deleteByRuleUniqueId: function(ruleUniqueId) {
        var record = cailaEntities.get(ruleUniqueId);
        if (record && record.recordId) {
            cailaEntities.deleteById(record.recordId);
        }
    },
    deleteAllRecords: function() {
        var records = cailaEntities.getAllRecords();
        if (!records) {
            return;
        }
        var idsToDelete = records.map(function(record) {
           return record.id;
        });

        cailaEntities.deleteById(idsToDelete);
    }
};
