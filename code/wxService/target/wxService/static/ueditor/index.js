$(function() {
	// 富文本编辑器
	UE.getEditor('myEditor');

	UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
	UE.Editor.prototype.getActionUrl = function(action) {
		if (action == '/resource/upload/images') {
			return basePath + 'resource/upload/images';
		} else {
			return this._bkGetActionUrl.call(this, action);
		}
	}
});