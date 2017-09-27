/**
 * Created by healex on 2016/12/1.
 */
function Encrypt(word,key,iv){
    key = CryptoJS.enc.Utf8.parse(key);
    iv  = CryptoJS.enc.Utf8.parse(iv);
    var srcs = CryptoJS.enc.Utf8.parse(word);
    var encrypted = CryptoJS.AES.encrypt(word, key, { iv: iv,mode:CryptoJS.mode.CBC,pad:CryptoJS.pad.ZeroPadding});
    encrypted = encodeURIComponent(encrypted);
    return encrypted.toString();
}

function Decrypt(word,key,iv){
	CryptoJS.enc.Utf8.parse(word);
    key = CryptoJS.enc.Utf8.parse(key);
    iv  = CryptoJS.enc.Utf8.parse(iv);
    var decrypt = CryptoJS.AES.decrypt(word, key, { iv: iv,mode:CryptoJS.mode.CBC,pad:CryptoJS.pad.ZeroPadding});
    return CryptoJS.enc.Utf8.stringify(decrypt).toString();
}