const{ createApp } = Vue;

createApp({
    data() {
        return {
            clientName: '',
            cards: [],
            debitCards: [],
            creditCards: []
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get(`http://localhost:8080/api/clients/current`)
                .then(res => {
                    this.client = res.data
                    this.cards = this.client.cards.sort((a,b) => a.id - b.id)
                    this.debitCards = this.client.cards.filter(card => card.type == "DEBIT")
                    this.creditCards = this.client.cards.filter(card => card.type == "CREDIT")
                }).catch(err => console.error(err))
        },
        changeCardColor(card) {
            if (card.color == 'SILVER') {
                return 'silverBg';
            } else if (card.color == 'GOLD') {
                return 'goldBg';
            } else if (card.color == 'TITANIUM') {
                return 'titaniumBg'
            }
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
					return axios.post('/api/logout')
						.then(response => {
							window.location.href = '/web/pages/index.html';
						})
						.catch(error => {
							console.log(error);
						});
				},
     
        }) 
    }
    }
}).mount("#app")