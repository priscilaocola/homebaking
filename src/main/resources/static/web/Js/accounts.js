
const { createApp } = Vue;

createApp({
    data() {
        return {
            accounts: [],
            client: [],
            loans: [],
			account: '',
			isActive:[],
			accountDelete:0,
			type: '',
			payTotal:0,
			loanID: [],
			quotas: 0,
        

        }
    },
    created() {
        this.loadData();
		this.accountsActive()
		this.myLoans()
    },
    methods: {
        loadData() {
            axios.get("/api/clients/current")
                .then(res => {
                    this.client = res.data;
                   }).catch(err => console.log(err))

        },
		myLoans() {
			axios.get(`/api/clients/current/loans`)
				.then(res => {
					this.loans = res.data.sort((a, b) => a.id - b.id)
				}).catch(err => console.log(err))
		},
		paymentsCalcu(amount, payments) {
			let division = amount / payments
			let formatDivision = parseFloat(division.toFixed(3))
			return formatDivision.toLocaleString()
		},
		accountsActive(){
            axios.get('/api/clients/current/accounts')
            .then(response => {
                this.isActive = response.data;
                this.isActive.sort((a,b)=> a.id - b.id)
                console.log(this.isActive)
            })
            .catch(error => console.log(error));

        },
	

        logout() {
            Swal.fire({
				title: 'Bye see you soon',
				imageUrl: '../asset/BYE.png',
				imageWidth: 400,
				imageHeight: 300,
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
				text: 'Select the type of account to create',
				input: 'select',
				inputOptions: {
					CURRENT: 'CURRENT',
					SAVINGS: 'SAVINGS',
				},
				inputPlaceholder: 'TYPE',
				showCancelButton: true,
				confirmButtonText: 'Create',
			}).then(result => {
				if (result.value) {
					this.type = result.value;
					axios.post(`/api/clients/current/accounts?type=${this.type}`)
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
				}
				
			});
		},
		deleteAccount(id) {
			Swal.fire({
				title: 'Do you want to delete the Account?',
				showCancelButton: true,
				confirmButtonText: 'Delete Account',
				denyButtonText: `Go back`,
			}).then((result) => {
				if (result.value) {
					this.accountDelete = id 
					axios.patch(`/api/clients/current/accounts?id=${this.accountDelete}`)
						.then(res => {
							Swal.fire({
								position: 'center',
								icon: 'success',
								title: 'Account Deleted',
								showConfirmButton: false,
								timer: 1500
							})
							setTimeout(() => {
								window.location.reload();
							}, 1800)
						}).catch(err => {
							Swal.fire({
								position: 'center',
								icon: 'error',
								title: ``,
								showConfirmButton: false,
								timer: 2000
							})
						})
				}
			})
		
		},
	
		
	},
}).mount("#app");



















































































































































































































































































