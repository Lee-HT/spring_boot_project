# 포트 리다이렉트

sudo iptables -t nat -L
sudo iptables -D PREROUTING -t nat -i eth0 -p tcp --dport 443 -j REDIRECT --to-port 6550
sudo iptables -A PREROUTING -t nat -i eth0 -p tcp --dport 443 -j REDIRECT --to-port 6550