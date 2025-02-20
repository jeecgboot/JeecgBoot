-- 升级积木BI到最新版
UPDATE onl_drag_comp SET status='0' WHERE parent_id = '0';
update onl_drag_page set type =0 where iz_template = '1';