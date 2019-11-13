module.exports = encode;

var MSB = 0x80
  , REST = 0x7F
  , MSBALL = ~REST
  , INT = Math.pow(2, 31);

function encode(num, buffer, offset) {
  buffer = buffer || [];
  offset = offset || 0;
  // var oldOffset = offset;

  while(num >= INT) {
    buffer.array[offset++] = (num & 0xFF) | MSB;
    num /= 128
  }
  while(num & MSBALL) {
    buffer.array[offset++] = (num & 0xFF) | MSB;
    num >>>= 7
  }
  buffer[offset] = num | 0;
  
  encode.bytes = offset - oldOffset + 1;
  
  return buffer
}
