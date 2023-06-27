let { createApp } = Vue;

createApp({
    data() {
        return {
            accounts: [],
            client: [],
            loans:[],

        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get("http://localhost:8080/api/clients/current")
                .then(res => {
                    this.client = res.data;
                    console.log(this.client)
                    this.accounts = this.client.accounts
                    this.accounts = this.client.accounts.sort((a,b) => a.id - b.id)
                    console.log(this.client.loans)
                    this.loans = this.client.loans.sort((a,b) => a.id - b.id)

                    this.format = new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD', }); 
                    this.accounts.forEach( e => { e.balance = this.format.format(e.balance) }); 
                    this.loans.forEach( e => { e.amount = this.format.format(e.amount)}); 
                }).catch(err => console.log(err))
            	
        },
        logout(){
            axios.post("/api/logout")
            .then((res) => {
                window.location.href="/web/pages/index.html"
            }).catch(err => console.log(err))
        }

    }
}).mount("#app");



















































































































































































































































































