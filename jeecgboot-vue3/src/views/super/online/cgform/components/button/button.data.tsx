import {Icon} from "@/components/Icon";

export function getButtonIconRender({text, buttonCode}) {
  if (!text) {
    return ''
  }
  const iconPrefix = buttonCode === 'aigc_mock_data' ? 'mdi:' : 'ant-design:';
  // @ts-ignore
  return <Icon icon={iconPrefix + text}/>;
}
