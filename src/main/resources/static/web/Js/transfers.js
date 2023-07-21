
const { createApp } = Vue

let app = createApp({
    data() {
        return {

            url: "/api/clients/current",
            clientes: [],
            accounts: [],
            radioTransfer: null,
            selectAccount: '',
            selectAccountTransferTo: '',
            amount: null,
            description: '',
            account2: '',

        }
    },
    mounted() {

        this.loadData()

    },

    methods: {
        loadData() {
            axios.get(this.url)
                .then((data) => {
                    this.clientes = data.data
                    this.accounts = this.clientes.accounts
                })
                .catch((error) => {
                    console.log(error);
                })
        },

        addTransfer() {
            axios.post('/api/transactions', `amount=${this.amount}&description=${this.description}&accountOrigin=${this.selectAccount}&accountDestiny=${this.account2}`)
                .then((response) => {
                    Swal.fire({
                        title: 'Successful Transaction',
                        imageUrl: '../asset/sucesso.png',
                        imageWidth: 400,
                        imageHeight: 300,
                        onClose: () => {
                            window.location.href = "/web/pages/accounts.html";
                        }
                    });
                })
                .catch((error) => Swal.fire({
                    icon: 'error',
                    text: 'Something went wrong!',
                }));
        },

        surePopUp() {
            Swal.fire({
                title: 'Are you sure you want to make this transaction?',
                text: "You won't be able to revert this!",
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes'
            }).then((result) => {
                if (result.value) {
                    this.addTransfer();
                }
            })


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
    }
}).mount('#app');