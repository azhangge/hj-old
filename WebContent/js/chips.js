var jq180 = $.noConflict();
function onAddTag(tag) {

	alert("Added a tag: " + tag);

}

function onRemoveTag(tag) {

	alert("Removed a tag: " + tag);

}

function onChangeTag(input, tag) {

	alert("Changed a tag: " + tag);

}

jq180(function() {

	jq180('#tags_1').tagsInput({
		width : 'auto',
		defaultText:'在此输入标签',
		'delimiter':';',
		maxCount:10,
		minInputWidth:'100px',
		maxChars:10
	});

	jq180('#tags_2').tagsInput(
			{

				width : 'auto',

				onChange : function(elem, elem_tags)

				{

					var languages = [ 'php', 'ruby', 'javascript' ];

					jq180('.tag', elem_tags).each(
							function()

							{

								if (jq180(this).text().search(
										new RegExp('\\b(' + languages.join('|')
												+ ')\\b')) >= 0)

									jq180(this).css('background-color',
											'yellow');

							});

				}

			});

	jq180('#tags_3').tagsInput({

		width : 'auto',

		// autocomplete_url:'test/fake_plaintext_endpoint.html'
		// //jquery.autocomplete (not jquery ui)

		autocomplete_url : 'test/fake_json_endpoint.html' // jquery ui
															// autocomplete
															// requires a json
															// endpoint

	});

	// Uncomment this line to see the callback functions in action

	// jq180('input.tags').tagsInput({onAddTag:onAddTag,onRemoveTag:onRemoveTag,onChange:
	// onChangeTag});

	// Uncomment this line to see an input with no interface for adding new
	// tags.

	// jq180('input.tags').tagsInput({interactive:false});

});

function checkAll(whichTable) {
	var idds = '#'+whichTable+'\\:'+'checkAll';
	var vv = $(idds).is(':checked');
	$('#'+whichTable+' '+'input[type="checkbox"]').each(function(index, element) {
		var idd = $(element).attr('id');
		if (idd.indexOf('checkAll') < 0) {
			$(element).prop('checked', vv);
		}
	});
}
