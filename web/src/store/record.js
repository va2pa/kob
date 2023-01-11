
export default {
    state: {
        is_record: false,
        a_steps: "",
        b_steps: "",
        record_loser: ""
    },
    mutations: {
        updateSteps(state, data) {
            state.a_steps = data.a_steps;
            state.b_steps = data.b_steps;
        },
        updateIsRecord(state, is_record) {
            state.is_record = is_record;
        },
        updateRecordLoser(state, record_loser) {
            state.record_loser = record_loser;
        }
    },
    actions: {
    },
    modules: {
    }
}