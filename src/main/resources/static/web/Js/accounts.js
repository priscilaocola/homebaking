let { createApp } = Vue;

createApp({
    data() {
        return {
            accounts: [],
            client: [],
            loans: [],
           

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
                    this.accounts = this.client.accounts.sort((a, b) => a.id - b.id)
                    console.log(this.client.loans)
                    this.loans = this.client.loans.sort((a, b) => a.id - b.id)

                    this.format = new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD', });
                    this.accounts.forEach(e => { e.balance = this.format.format(e.balance) });
                    this.loans.forEach(e => { e.amount = this.format.format(e.amount) });
                }).catch(err => console.log(err))

        },
        logout() {
            Swal.fire({
				title: 'Are you sure that you want to log out',
				inputAttributes: {
					autocapitalize: 'off',
				},
				showCancelButton: true,
				confirmButtonText: 'Sure',
				showLoaderOnConfirm: true,
				preConfirm: login => {
					return axios
						.post('/api/logout')
						.then(response => {
							window.location.href = '/web/pages/index.html';
						})
						.catch(error => {
							Swal.showValidationMessage(`Request failed: ${error}`);
						});
				},
			});
		},
        createAccount() {
			Swal.fire({
				title: 'Are you sure you want to create another account?',
				inputAttributes: {
					autocapitalize: 'off',
				},
				showCancelButton: true,
				confirmButtonText: 'Sure',
				showLoaderOnConfirm: true,
				preConfirm: login => {
					return axios
						.post('/api/clients/current/accounts')
						.then(response => {
							window.location.href = '/web/pages/accounts.html';
						})
						.catch(error => {
							Swal.fire({
								icon: 'error',
								text: error.response.data,
								confirmButtonColor: '#D9010193',
							});
						});
				},
				
			});
		},
	},
}).mount("#app");



















































































































































































































































































