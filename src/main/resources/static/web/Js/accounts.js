let { createApp } = Vue;

createApp({
    data() {
        return {
            clientName: '',
            cuentaUno: [],
            cuentaDos: [],
            selectCuenta: ''
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get("http://localhost:8080/api/clients/1")
                .then(res => {
                    this.client = res.data
                    this.clientName = this.client.firstName + ' ' + this.client.lastName
                    this.cuentaUno = res.data.accounts
                    console.log(this.cuentaUno)
                    this.cuentaDos = res.data.accounts
                }).catch(err => console.log(err))
                
        },
    }
}).mount("#app")
