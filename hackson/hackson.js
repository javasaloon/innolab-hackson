function main(input)
{
  var para = JSON.parse(input);
  if (para.operation=='init') {
      var account = callBackGetAccountInfo('a0010d0a5ac88d52a4cd5c2c0d7f62c456741ea0b11768');

        var transaction = {
          'operations' : [
            {
              "type" : 2,
              "issue_asset" :
              {
                  "amount" : 1000
              }
            }
          ]
        };
        var result = callBackDoOperation(transaction);

      return account;
  }

}
