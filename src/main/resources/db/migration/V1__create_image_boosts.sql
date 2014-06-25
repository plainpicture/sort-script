
create table image_boosts(id int primary key auto_increment, primary_country_id int, secondary_country_id int, boost float, created_at datetime, updated_at datetime);
create table creator_boosts(id int primary key auto_increment, primary_country_id int, secondary_country_id int, boost float, created_at datetime, updated_at datetime);

