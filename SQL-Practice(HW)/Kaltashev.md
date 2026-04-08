https://sql-academy.org/ru/trainer/tasks/74
SELECT id, CASE WHEN has_internet = 1 THEN 'YES' ELSE 'NO' END AS has_internet
FROM Rooms;

https://sql-academy.org/ru/trainer/tasks/56
DELETE from Trip where town_from = 'Moscow'

https://sql-academy.org/ru/trainer/tasks/114
SELECT Pilots.name
FROM Flights
JOIN Pilots ON Flights.second_pilot_id = Pilots.pilot_id
WHERE Flights.destination = 'New York'
AND MONTH(Flights.flight_date) = 8
AND YEAR(Flights.flight_date) = 2023;

https://sql-academy.org/ru/trainer/tasks/19
SELECT DISTINCT FamilyMembers.status
FROM Payments
JOIN FamilyMembers ON Payments.family_member = FamilyMembers.member_id
JOIN Goods ON Payments.good = Goods.good_id
WHERE Goods.good_name = 'potato'

https://sql-academy.org/ru/trainer/tasks/21
SELECT Goods.good_name
FROM Payments
JOIN Goods ON Payments.good = Goods.good_id
GROUP BY Goods.good_name
HAVING COUNT(*) > 1;

https://sql-academy.org/ru/trainer/tasks/32
SELECT FLOOR(AVG(TIMESTAMPDIFF(YEAR, birthday, CURDATE()))) AS age
FROM FamilyMembers;