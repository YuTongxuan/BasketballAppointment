const log = console.log;

const comm = {
    setCookie: function(userOrId) {
        let {id = null} = userOrId;
        if(id == null && typeof(userOrId) == 'number') {
            id = userOrId;
        }
        if (id == null) {
            throw 'argument does not contain property "id"';
        }
        const expireDate = new Date();
        expireDate.setTime(expireDate.getTime() + (100*24*60*60*1000));
        document.cookie = `userId=${id};expires=${expireDate.toGMTString()}`;
    },
    getUserIdFromCookie: function() {
        const filtered = document.cookie.split(';').filter((x) => x.startsWith('userId='));
        if (filtered.length == 0) {
            return null;
        }
        const keyValue = filtered[0];
        const value = keyValue.split('=')[1].trim();
        return parseInt(value);
    },
    clearCookie: function() {
        const expireDate = new Date();
        expireDate.setTime(expireDate.getTime() - 1);
        document.cookie = `userId=1;expires=${expireDate.toGMTString()}`;
    },
    getDeclaredErrorMessage: function(response) {
        const defaultMsg = "An unknown error occurs.";
        if (!response || !response.responseJSON) {
            return defaultMsg;
        }
        let msg = response.responseJSON.message;
        if (!msg) {
            return defaultMsg;
        }
        msg = msg.substring(msg.lastIndexOf(':') + 1).trim();
        msg = msg.charAt(0).toUpperCase() + msg.substring(1);
        if (!msg.endsWith('.')) {
            msg += '.';
        }
        return msg;
    },
    transferAbilityToArray: function(stringAbility) {
        return stringAbility.split("|").map((x) => parseInt(x));
    }
};