const{ createApp } = Vue;

createApp({
    data() {
        return {
            clientName: '',
            cards: [],
            debitCards: [],
            creditCards: [],
            cardsActive: [],
            renewCard: '',
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get(`/api/clients/current`)
                .then(res => {
                    this.client = res.data
                    this.cards = this.client.cards.sort((a,b) => a.id - b.id)
                    this.debitCards = this.client.cards.filter(card => card.type == "DEBIT" && card.active)
                    this.creditCards = this.client.cards.filter(card => card.type == "CREDIT" && card.active)
                    this.cardsActive = this.cards.filter(card => card.active);
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
				title: 'Bye see you soon',
				imageUrl: '../asset/BYE.png',
				imageWidth: 400,
				imageHeight: 300,
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
    cardDelete(id) {
        Swal.fire({
            title: 'Are you sure you want to delete this card?',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'DELETE',
            showLoaderOnConfirm: true,
            preConfirm: deleteCards => {
                return axios.put(`/api/clients/current/cards/${id}`)
                    .then(response => {
                        window.location.href = '/web/pages/cards.html';
                    })
                    .catch(error => {
                        Swal.fire({
                            icon: 'error',
                            text: error.response.data,
                            confirmButtonColor: '#7c601893',
                        });
                    });
            },
        });
    },
    renewCard(renewCard) {
        Swal.fire({
            title: 'Do you want to renew the card?',
            showDenyButton: true,
            showCancelButton: true,
            confirmButtonText: 'Renew',
            denyButtonText: `Don't renew`,
        }).then((result) => {
            if (result.isConfirmed) {
                this.renewCard = renewCard
                axios.post(`/api/cards/renew?number=${this.renewCard}`)
                    .then(res => {
                        Swal.fire({
                            position: 'center',
                            icon: 'success',
                            title: 'Renewed card ok',
                            showConfirmButton: false,
                            timer: 1500
                        })
                        setTimeout(() => {
                            window.location.reload()
                        }, 1900)
                    }).catch(err => {
                        this.renewCardError = err.response.data
                        Swal.fire({
                            position: 'center',
                            icon: 'error',
                            title: `${this.renewCardError}`,
                            showConfirmButton: false,
                            timer: 1500
                        })
                    })
            } else if (result.isDenied) {
                Swal.fire({
                    position: 'center',
                    icon: 'info',
                    title: 'Changes are not saved',
                    showConfirmButton: false,
                    timer: 1500
                })
            }
        })
    },
    }
}).mount("#app")