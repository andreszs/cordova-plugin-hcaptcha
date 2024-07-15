var HCaptchaProxy = {
  verify: function(successCallback, errorCallback, args) {
    var siteKey = '';
    var params = args && args[0] ? args[0] : {};

    if (typeof params.siteKey === 'string' && params.siteKey.length > 0) {
      siteKey = params.siteKey;
    } else {
      errorCallback('args.siteKey is not set');
      return;
    }

    function loadHcaptchaScript(callback) {
      if (window.hcaptcha) {
        callback();
        return;
      }

      var script = document.createElement('script');
      script.src = 'https://js.hcaptcha.com/1/api.js?render=explicit&onload=hcaptchaOnloadCallback';
      script.onerror = function() {
        errorCallback('Failed to load hCaptcha script');
      };
      document.head.appendChild(script);

      // Define the onload callback
      window.hcaptchaOnloadCallback = callback;
    }

    loadHcaptchaScript(function() {
      // Create the hCaptcha container element dynamically
      var hcaptchaContainer = document.createElement('div');
      document.body.appendChild(hcaptchaContainer);

      var widgetParams = {
        'sitekey': siteKey,
        'size': 'invisible',
        'callback': function(token) {
          if (typeof successCallback === 'function') {
            successCallback(token);
          }
          document.body.removeChild(hcaptchaContainer);
        },
        'error-callback': function(error) {
          if (typeof errorCallback === 'function') {
            errorCallback(error);
          }
          document.body.removeChild(hcaptchaContainer);
        },
		'close-callback': function() {
          if (typeof errorCallback === 'function') {
            errorCallback('hCaptcha failed: Challenge Closed(30)');
          }
          document.body.removeChild(hcaptchaContainer);
        },
		'chalexpired-callback': function() {
          if (typeof errorCallback === 'function') {
            errorCallback('hCaptcha failed: Session Timeout(15)');
          }
          document.body.removeChild(hcaptchaContainer);
        },
        'hl': params.locale || 'en'
      };

      if (params.orientation) {
        widgetParams.orientation = params.orientation;
      }

      if (params.loading) {
        widgetParams.loading = params.loading;
      }

      var widgetId = hcaptcha.render(hcaptchaContainer, widgetParams);

      // Execute the hCaptcha challenge
      hcaptcha.execute(widgetId);
    });
  }
};


module.exports = HCaptchaProxy;

require('cordova/exec/proxy').add('Hcaptcha', HCaptchaProxy);
