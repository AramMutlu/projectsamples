module.exports = read;

let Buffer = require('../../../global/model/Buffer')

var MSB = 0x80
  , REST = 0x7F;

function read(buffer, offset) {
  var res    = 0
    , shift  = 0
    , counter = offset
    , b
    , l = buffer.length;

  do {
    if (counter >= l) {
      throw new RangeError('Could not decode varint')
    }
    console.log("Buffer: " + buffer)
    b = buffer.getNext();
    res += shift < 28
      ? (b & REST) << shift
      : (b & REST) * Math.pow(2, shift);
    shift += 7
  } while (b >= MSB);

  return res
}
