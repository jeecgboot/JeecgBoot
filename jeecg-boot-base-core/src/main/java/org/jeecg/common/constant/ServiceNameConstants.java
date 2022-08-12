/*
 *
 *  *  Copyright (c) 2019-2020, 冷冷 (wangiegie@gmail.com).
 *  *  <p>
 *  *  Licensed under the GNU Lesser General Public License 3.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *  <p>
 *  * https://www.gnu.org/licenses/lgpl.html
 *  *  <p>
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.jeecg.common.constant;

/**
 * @author scott
 * @date 2019年05月18日
 * 服务名称
 */
public interface ServiceNameConstants {

	/**
	 * 微服务名：系统管理模块
	 */
	String SERVICE_SYSTEM = "jeecg-system";
	/**
	 * 微服务名：Demo模块
	 */
	String SERVICE_DEMO = "jeecg-demo";

	/**
	 * gateway通过header传递根路径 basePath
	 */
	String X_GATEWAY_BASE_PATH = "X_GATEWAY_BASE_PATH";

}
