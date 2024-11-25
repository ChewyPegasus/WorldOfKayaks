$(document).ready(function() {
    $('.add-to-cart').click(function() {
        var productId = $(this).data('product-id');
        $.post('/cart/add/' + productId)
            .done(function() {
                alert('Product added to cart');
                updateCartCount();
            })
            .fail(function() {
                alert('Error adding product to cart');
            });
    });

    function updateCartCount() {
        $.get('/cart/count', function(count) {
            $('#cart-count').text(count);
        });
    }
});