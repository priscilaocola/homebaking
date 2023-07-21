let { createApp } = Vue;
createApp({
    data() {
        return {
            getId: "", 
            loan: {},
            idLoan: null,
            accountPay: '',
            accounts: [],
            errMsg: ''
        }
    },
    created() {
        this.getLoanDetails()
        this.getActiveAccounts()
    },
    methods: {
        loanPaymentRequest() {
            Swal.fire({
                title: 'Do you want to make a payment for this loan?',
                showDenyButton: true,
                showCancelButton: true,
                confirmButtonText: 'Confirm payment',
                denyButtonText: `Go back`,
            }).then((res) => {
                if (res.isConfirmed) {
                    axios.post(`/api/clientLoan/payments?loanToPay=${this.idLoan}&account=${this.accountPay}`)
                        .then(res => {
                            Swal.fire({
                                position: 'center',
                                icon: 'success',
                                title: `Payment correct`,
                                showConfirmButton: false,
                                timer: 1500
                            })
                            setTimeout(() => {
                                window.location.href = '/web/pages/accounts.html'
                            }, 1900)
                        }).catch(err => {
                            this.errMsg = err.response.data
                            Swal.fire({
                                position: 'center',
                                icon: 'error',
                                title: `${this.errMsg}`,
                                showConfirmButton: false,
                                timer: 1500
                            })
                        })
                }
            })
        },
        getActiveAccounts() {
            axios.get(`/api/clients/current/accounts`)
                .then(res => {
                    this.accounts = res.data.sort((a, b) => a.id - b.id)
                }).catch(err => console.log(err))
        },
        getLoanDetails() {
            this.  getId = new URLSearchParams(location.search).get('id')
            axios.get(`/api/clientLoans/${this.getId}`)
                .then(res => {
                    this.loan = res.data
                    this.idLoan = res.data.id
                    console.log(this.loan)
                }).catch(err => {
                    console.log(err)
                })
        },
        paymentsCalculator(amount, payments) {
            let division = amount / payments
            let formatDivision = parseFloat(division.toFixed(2))
            return formatDivision.toLocaleString()
        },
        sessionLogOut() {
            axios.post("/api/logout")
                .then(res => {
                    if (res.status == 200) {
                        Swal.fire({
                            position: 'center',
                            icon: 'success',
                            title: 'Bye bye!',
                            showConfirmButton: false,
                            timer: 1500
                        })
                        setTimeout(() => {
                            window.location.href = "/web/pages/index.html";
                        }, 1800)
                    }
                    console.log(res)
                }).catch(err => { console.log(err) })
        }
    }
}).mount("#app")