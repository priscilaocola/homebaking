const { createApp } = Vue;

createApp({
	data() {
		return {
			loans: [],
			accounts: [],
			account: '',
			payments: '',
			amount: 0,
			loanID: '',
			paymentsFilter: '',
			loanType: '',
			cuotas: 0,
		};
	},
	created() {
		this.loanData();
		this.accountsData();
	},
	methods: {
		loanData() {
			axios.get('/api/loans')
				.then(response => {
					this.loans = response.data;
				})
				.catch(error => console.log(error));
		},
		accountsData() {
			axios.get('/api/clients/current')
				.then(response => {
					this.accounts = response.data.accounts;
				})
				.catch(error => console.log(error));
		},


		filterPayments() {
			this.paymentsFilter = this.loans.filter(loan => {
				return this.loanType.includes(loan.name);
			})[0];
		},
		loanCreate() {
			this.loanID = this.paymentsFilter.id;
			Swal.fire({
				title: 'Are you sure you want to apply for this loan?',
				showCancelButton: true,
				confirmButtonText: 'Confirmed',
				confirmButtonColor: '#7c601893',
				preConfirm: () => {
					return axios.post('/api/loans', {
						loanID: `${this.loanID}`,
						amount: `${this.amount}`,
						payments: `${this.payments}`,
						accountDestiny: `${this.account}`,
					})
						.then(response => {
							Swal.fire({
								position: 'center',
								text: 'LOAN APPROVED',
								showConfirmButton: false,
								timer: 2000,
							
							})
							window.location.href = "/web/pages/accounts.html";
						}).catch((error) => Swal.fire({
							icon: 'error',
							text: 'please complete all the data!',
							text: error.response.data,
							confirmButtonColor: '#7c601893',
						}));
				},
			});
		},
		interests() {
			this.cuotas = (this.amount * 1.2) / this.payments;
		},
		logout() {
            Swal.fire({
				title: 'Bye see you soon',
				imageUrl: '../asset/BYE BYE.png',
				imageWidth: 400,
				imageHeight: 250,
				imageAlt: 'Custom image',
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
     })
	},
},
}).mount('#app');
