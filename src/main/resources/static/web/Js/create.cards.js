const { createApp } = Vue;

createApp({
    data() {
        return {
            color: '',
            type: '',


        };
    },
    created() {},

    methods: {
        createCards() {
            axios.post('/api/clients/current/cards', `type=${this.type}&color=${this.color}`)
                .then(response => {
                    Swal.fire({
                        position: 'center',
                        text: 'The card was created successfully',
                        showConfirmButton: false,
                       time:3000
                    });
                    window.location.href = '/web/pages/cards.html';
                }
                ).catch((error) => Swal.fire({
                    icon: 'error',
                    text: 'Something went wrong!',
                }));
            },
            cardsNews() {
                Swal.fire({
                    title: 'Are you sure you want to create this card?',
                    text: "You won't be able to revert this!",
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Yes'
                }).then((result) => {
                    if (result.value) {
                        this.createCards();
                    }
                })
               
    
    
            }
    },
}).mount('#app');