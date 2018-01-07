var utils = {
    json: {
        getKeys: function (data) {
            return Object.keys(data);
        },
        getValues: function (data) {
            return Object.values(data);
        },
        getLength: function (data) {
            return Object.keys(data).length;
        }
    },
    // https://stackoverflow.com/questions/995183/how-to-allow-only-numeric-0-9-in-html-inputbox-using-jquery#answer-32004562
    numberOnly: function (selector) {
        // Replace characters with '' except number, period, comma, plus sign, minus sign
        selector.value = selector.value.replace(/[^\d\.\,\+\-]/g, '');
    },
    // https://stackoverflow.com/questions/149055/how-can-i-format-numbers-as-money-in-javascript#answer-1323064
    numberFormat: function (number, decimals, dec_point, thousands_sep) {

        var n = number, prec = decimals;

        var toFixedFix = function (n, prec) {
            var k = Math.pow(10, prec);
            return (Math.round(n * k) / k).toString();
        };

        n = !isFinite(+n) ? 0 : +n;
        prec = !isFinite(+prec) ? 0 : Math.abs(prec);
        var sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep;
        var dec = (typeof dec_point === 'undefined') ? '.' : dec_point;

        var s = (prec > 0) ? toFixedFix(n, prec) : toFixedFix(Math.round(n), prec);
        //fix for IE parseFloat(0.55).toFixed(0) = 0;

        var abs = toFixedFix(Math.abs(n), prec);
        var _, i;

        if (abs >= 1000) {
            _ = abs.split(/\D/);
            i = _[0].length % 3 || 3;

            _[0] = s.slice(0, i + (n < 0)) +
                    _[0].slice(i).replace(/(\d{3})/g, sep + '$1');
            s = _.join(dec);
        } else {
            s = s.replace('.', dec);
        }

        var decPos = s.indexOf(dec);
        if (prec >= 1 && decPos !== -1 && (s.length - decPos - 1) < prec) {
            s += new Array(prec - (s.length - decPos - 1)).join(0) + '0';
        }
        else if (prec >= 1 && decPos === -1) {
            s += dec + new Array(prec).join(0) + '0';
        }
        return s;
    },
    dataTables: {
        language: {
            en: {
                sEmptyTable: "No data available in table",
                sInfo: "Showing _START_ to _END_ of _TOTAL_ entries",
                sInfoEmpty: "Showing 0 to 0 of 0 entries",
                sInfoFiltered: "(filtered from _MAX_ total entries)",
                sInfoPostFix: "",
                sInfoThousands: ",",
                sLengthMenu: "Show _MENU_ entries",
                sLoadingRecords: "Loading...",
                sProcessing: "Processing...",
                sSearch: "Search:",
                sSearchPlaceholder: "Search",
                sUrl: "",
                sZeroRecords: "No matching records found",
                oPaginate: {
                    sFirst: "First",
                    sLast: "Last",
                    sNext: "Next",
                    sPrevious: "Previous"
                },
                oAria: {
                    sSortAscending: ": activate to sort column ascending",
                    sSortDescending: ": activate to sort column descending"
                }
            },
            "in": {
                sEmptyTable: "Tidak ada data pada tabel",
                sInfo: "Menampilkan _START_ sampai _END_ dari _TOTAL_ entri",
                sInfoEmpty: "Menampilkan 0 sampai 0 dari 0 entri",
                sInfoFiltered: "(disaring dari _MAX_ entri keseluruhan)",
                sInfoPostFix: "",
                sInfoThousands: ".",
                sLengthMenu: "Tampilkan _MENU_ entri",
                sLoadingRecords: "Memuat...",
                sProcessing: "Sedang memproses...",
                sSearch: "Cari:",
                sSearchPlaceholder: "Cari",
                sUrl: "",
                sZeroRecords: "Tidak ditemukan data yang sesuai",
                oPaginate: {
                    sFirst: "Pertama",
                    sPrevious: "Sebelumnya",
                    sNext: "Selanjutnya",
                    sLast: "Terakhir"
                },
                oAria: {
                    sSortAscending: ": aktifkan untuk mengurutkan kolom secara meningkat",
                    sSortDescending: ": aktifkan untuk mengurutkan kolom secara menurun"
                }
            }
        }
    },
    randomAlphanumeric: function (length) {
        var text = "";
        var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (var i = 0; i < length; i++) {
            text += possible.charAt(Math.floor(Math.random() * possible.length));
        }
        return text;
    },
    jqueryValidate: function(selector, rules) {
    	if (typeof $(selector).validate === 'function') {
    		$(selector).validate({
    			rules: rules,
    			errorElement: "em",
    			errorPlacement: function (error, element) {
    				// Add the 'help-block' class to the error element
    				error.addClass("help-block");
    				
    				if (element.prop("type") === "checkbox") {
    					error.insertAfter(element.parent("label"));
    				} else if(element.parent().attr('class').indexOf('input-group') !== -1) {
    					error.insertAfter(element.parent());
    				} else if(element.attr('class').indexOf('select2') !== -1) {
    					error.appendTo(element.parent());
    				} else {
    					error.insertAfter(element);
    				}
    			},
    			highlight: function (element, errorClass, validClass) {
    				$(element).parents(".form-group").addClass("has-error");
    			},
    			unhighlight: function (element, errorClass, validClass) {
    				$(element).parents(".form-group").removeClass("has-error");
    			}
    		});
    	} else {
    		alert("Script required: jQuery validation plugin.");
    	}
    },
    sweetAlert: {
    	options: {
    		title: "Success",
            text: "Success!",
            type: "success",
            showCancelButton: true,
            confirmButtonText: "Yes",
            cancelButtonText: "No",
            closeOnConfirm: false
    	},
    	confirmation: function(options, onConfirm, onCancel) {
    		if (typeof swal === "function") {
    			if (options !== undefined) {
        			for (var key in options) {
        				utils.sweetAlert.options[key] = options[key];	
        			}	
        		}
    			utils.sweetAlert.options.type = "warning";
    			swal(utils.sweetAlert.options, function(confirmed) {
    				if (confirmed) {
                    	if(onConfirm && (typeof onConfirm === "function")) {
                    		onConfirm();
                        }
                        swal.close();
                    } else {
                        if(onCancel && (typeof onCancel === "function")) {
                            onCancel();
                        }
                    }
                });
    		} else {
    			alert("Script required: SweetAlert.");
    		}
    	}
    }
};