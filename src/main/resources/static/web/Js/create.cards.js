const {createApp} = Vue;

createApp({
	data() {
		return {
			color: '',
			type: '',
    

		};
	},
    created(){

    },

	methods: {

		createCards() {
            console.log(this.type,this.color)
			axios.post('/api/clients/current/cards',`type=${this.type}&color=${this.color}`)
				.then(response => {
                     window.location.href = '/web/pages/cards.html'
                    console.log("createCards")
                }
            ).catch(err =>
                console.log(err))
        },
    },
    }).mount('#app');