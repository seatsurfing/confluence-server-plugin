(function ($) { // this closure helps us keep our variables to ourselves.
    // This pattern is known as an "iife" - immediately invoked function expression

    // form the URL
    var url = AJS.contextPath() + "/rest/seatsurfing-admin/1.0/";

    // wait for the DOM (i.e., document "skeleton") to load. This likely isn't necessary for the current case,
    // but may be helpful for AJAX that provides secondary content.
    $(document).ready(function () {
        // request the config information from the server
        $.ajax({
            url: url,
            dataType: "json"
        }).done(function (config) { // when the configuration is returned...
            // ...populate the form.
            $("#orgId").val(config.orgId);
            $("#bookingUiUrl").val(config.bookingUiUrl);
            $("#sharedSecret").val(config.sharedSecret);

            AJS.$("#admin").submit(function(e) {
                e.preventDefault();
                $("#action-messages").hide();
                updateConfig();
            });
        });
    });

})(AJS.$ || jQuery);

function updateConfig() {
    AJS.$.ajax({
        url: AJS.contextPath() + "/rest/seatsurfing-admin/1.0/",
        type: "PUT",
        contentType: "application/json",
        data: JSON.stringify({
            "orgId": AJS.$("#orgId").attr("value"),
            "bookingUiUrl": AJS.$("#bookingUiUrl").attr("value"),
            "sharedSecret": AJS.$("#sharedSecret").attr("value")
        }),
        processData: false,
        success: function() {
            $("#action-messages").show();
        }
    });
}