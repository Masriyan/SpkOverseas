const autoNumericOptionsRupiah = {
    digitGroupSeparator        : '.',
    decimalCharacter           : ',',
    decimalCharacterAlternative: '.',
//    currencySymbol             : 'Rp. ',
    vMin: '0', //control minimum value
    allowDecimalPadding        : false,
    currencySymbolPlacement    : AutoNumeric.options.currencySymbolPlacement.prefix,
    roundingMethod             : AutoNumeric.options.roundingMethod.halfUpSymmetric,
};

function toNumber(currency){

    currency = currency.replace('Rp. ', '');
    currency = currency.replace('Rp.', '');
    currency = currency.replace(',00', '');
    return currency.split('.').join("");
}

function toCurrency(amount){
//    return 'Rp. ' + amount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".")+',00';
    return '' + amount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".")+',00';
}