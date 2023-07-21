let { createApp } = Vue;

createApp({
    data() {
        return {
            accounts: [],
            client: [],
            loans: [],
			account: '',
			accountActive: [],
			accountToDelete: '',
			type: '',
			payTotal:0,
			loanID: [],
			quotas: 0,
        

        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get("/api/clients/current")
                .then(res => {
                    this.client = res.data;
                    this.accounts = this.client.accounts
                    this.accounts = this.client.accounts.sort((a, b) => a.id - b.id)
                    this.loans = this.client.loans.sort((a, b) => a.id - b.id)
					this.accountActive = this.accounts.filter(account => account.activeAccount);
                    this.format = new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD', });
                    this.accounts.forEach(e => { e.balance = this.format.format(e.balance) });
                    this.loans.forEach(e => { e.amount = this.format.format(e.amount) });
                }).catch(err => console.log(err))

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
		deleteAccount(accountNumber) {
			Swal.fire({
				title: 'Do you want to delete the Account?',
				showDenyButton: true,
				showCancelButton: true,
				confirmButtonText: 'Delete Account',
				denyButtonText: `Go back`,
			}).then((res) => {
				if (res.value) {
					this.accountToDelete = accountNumber
					axios.patch(`/api/clients/current/accounts?number=${this.accountToDelete}`)
						.then(res => {
							this.accounts = this.accounts.filter(account => account.number !== this.accountToDelete);
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
		loanFilter(id) {
			this.loanID = this.loans.filter(loan => loan.id == id)[0];
			console.log(this.loanID);
			this.quotas = this.loanID.totalAmount / this.loanID.payments;
			console.log(this.quotas);
			this.payTotal = this.loanID.totalAmount;
			console.log(this.payTotal);
		},
		loanPay() {
			Swal.fire({
				title: 'Are you sure you want to repay this loan?',
				showCancelButton: true,
				confirmButtonColor: '#3085d6',
				confirmButtonText: 'SURE',
			}).then(result => {
				if (result.value) {
					axios
						.post( `/api/loans/pay?id=${this.loanID.id}&account=${this.account}&amount=${this.amount}`)
						.then(response => {
							Swal.fire({
								icon: 'success',
								text: 'It was paid correctly',
								showConfirmButton: false,
								timer: 2000,
							}).then(() => (window.location.href = '/web/page/accounts.html'));
						})
						.catch(error => {
							Swal.fire({
								icon: 'error',
								text: error.response.data,
								confirmButtonColor: '#7c601893',
							});
						});
				}
			});
		}
		
	},
}).mount("#app");



















































































































































































































































































