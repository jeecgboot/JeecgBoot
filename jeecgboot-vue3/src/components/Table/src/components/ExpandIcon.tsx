import { BasicArrow } from '/@/components/Basic';

export default () => {
  return (props: Recordable) => {
    if (!props.expandable) {
      if (props.needIndentSpaced) {
        return <span class="ant-table-row-expand-icon ant-table-row-spaced" />;
      } else {
        return <span />;
      }
    }
    return (
      <BasicArrow
        style="margin-right: 8px"
        iconStyle="margin-top: -2px;"
        onClick={(e: Event) => {
          props.onExpand(props.record, e);
        }}
        expand={props.expanded}
      />
    );
  };
};
