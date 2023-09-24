local count = redis.call('get', KEYS[1])
if count == false then
    redis.call('set', KEYS[1], ARGV[2])
    redis.call('expire', KEYS[1], 86400)
    count = ARGV[2]
else
    count = redis.call('incr', KEYS[1])
end

if tonumber(count) >= tonumber(ARGV[1]) then
    redis.call('del', KEYS[1])
    return 1
else
    return 0
end





