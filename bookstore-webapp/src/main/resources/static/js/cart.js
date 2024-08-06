document.addEventListener('alpine:init', () => {
    Alpine.data('initData', () => ({
        cart: { items: [], totalAmount: 0 },
        orderForm: {
            customer: {
                name: "Andrew",
                email: "test@gmail.com",
                phone: "6476890900"
            },
            deliveryAddress: {
                address_line1: "street# 200",
                addressLine2: "Balliol st",
                city:"Toronto",
                state: "ON",
                zipCode: "M4S1C6",
                country: "Canada"
            }
        },

        init() {
            updateCartItemCount();
            this.loadCart();
            this.cart.totalAmount = getCartTotal();
        },
        loadCart() {
            this.cart = getCart()
        },
        updateItemQuantity(code, quantity) {
            updateProductQuantity(code, quantity);
            this.loadCart();
            this.cart.totalAmount = getCartTotal();
        },
        removeCart() {
            deleteCart();
        },
        createOrder() {
            let order = Object.assign({}, this.orderForm, {items: this.cart.items});
            //console.log("Order ", order);

            $.ajax ({
                url: '/api/orders',
                type: "POST",
                dataType: "json",
                contentType: "application/json",
                data : JSON.stringify(order),
                success: (resp) => {
                    //console.log("Order Resp:", resp)
                    this.removeCart();
                    //alert("Order placed successfully")
                    window.location = "/orders/"+resp.orderNumber;
                }, error: (err) => {
                    console.log("Order Creation Error:", err)
                    alert("Order creation failed")
                }
            });
        },
    }))
});