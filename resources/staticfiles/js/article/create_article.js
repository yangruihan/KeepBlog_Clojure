$(document).ready(function () {
	$('#back_btn').click(function () {
		location.href = '/';
		return false;
	});
	
	var simplemde = new SimpleMDE({
		autoDownloadFontAwesome: true,
		element: $("#markdown_editor")[0],
		tabSize: 4
	});
});