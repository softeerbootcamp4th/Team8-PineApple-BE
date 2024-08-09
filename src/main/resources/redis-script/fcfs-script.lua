local is_set = redis.call('SADD', KEYS[1] ,ARGV[1])
if is_set == 0
then
    return -1;
end
local order = redis.call('SCARD', KEYS[1])
if order <= tonumber(ARGV[2])
then
    return order
end
return 0