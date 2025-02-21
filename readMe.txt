1. /*https://medium.com/@zorbeytorunoglu/mapstruct-android-872b54a4dd10
* MapStruct Android
* All code is adapted from the article
* accessed on the 14/01/2025*/



2. /*https://chatgpt.com
* prompt: build a search query for my checkInventory koitlin screen use this use case for it
class GetInventoryUseCase(
    private val inventoryClient: InventoryClient

) {

    suspend fun execute(): List<Inventory> {
        return inventoryClient.getInventory()
    }
} this will return a list of names have it so I can search this list and it will populate entries*/



3. /* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 5/11/2024 accessed
All code here is adapted from the video




4. https://chatgpt.com
prompt: build a view model for my kotilin screen using these use cases to get the data package com.example.delitelligencefrontend.domain

import com.example.delitelligence.type.DeliSaleInputDto
import com.example.delitelligence.type.InventoryAdjustmentInputDto

class PostAdjustmentUseCase(
    private val inventoryClient: InventoryClient
) {
    suspend fun execute(input: InventoryAdjustmentInputDto): String? {
        return inventoryClient.createAdjustment(input)
    }
} and package com.example.delitelligencefrontend.domain

import com.example.delitelligencefrontend.model.Product
import com.example.delitelligencefrontend.model.Supplier

class GetSupplierUseCase(
    private val supplierClient: SupplierClient
) {
    suspend fun execute(): List<Supplier> {
        return supplierClient.getAllSuppliers()
    }
} and    suspend fun execute(): List<Product> {
        return productClient.getAllProducts()
    }

*/


5. /* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 5/11/2024 accessed
All code here is adapted from the video
The code for okayHttp injections I learned how to do through this video as well*/



6. /* https://www.youtube.com/watch?v=6_wK_Ud8--0&ab_channel=PhilippLackner
The Jetpack Compose Beginner Crash Course for 2023 💻 (Android Studio Tutorial)
Date 6/11/2024 accessed
All code here is adapted from the video*/


7. /*https://chatgpt.com
prompt: 'Create a left and right quadrant and have the left quadrant create a product card with a name and image placeholder for my list of objects
the right quadrant then build a button for finishing the product'
*/


8. /*https://chatgpt.com
prompt: 'building a screen and dividing it into three quadrants top left, bottom left and Right and create product cards that I can fill with my objects
that show the name and image. Also create a circle shape on the right quadrant that I can use to store the weight data retrieved from the scales'
*/



9. /*https://chatgpt.com
prompt: 'building a screen and dividing it into three quadrants top left, bottom left and Right and create product cards that I can fill with my objects
that show the name and image. Also create a circle shape on the right quadrant that I can use to store the weight data retrieved from the scales'
Used the code from the prompt to make the similar elements.
*/

10. /* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 10/10/2024 accessed */


11. /* https://www.youtube.com/watch?v=qyG-SDfYNBE&t=2981s&ab_channel=KApps
Android Bluetooth Low Energy Guide (Connect to a BLE sensor) - Android Studio Tutorial with Kotlin
Date 6/11/2024 accessed */

12. # https://github.com/lucapinello/pydecentscale
# Used Open Source library here

13. # https://chatgpt.com
# prompt 'How to build an API service in python using flask library'
# prompt 'How to make service thread safe scales cant find the scale when a second request to activate is sent'

14.
/*https://chatgpt.com
prompt: 'build a screen that will display the products data from the graphql request and display it in rows
and add, edit and delete buttons mapped by the viewModel aswell as using the search query made in the view model
to search the products.
*/

15.
/*https://www.youtube.com/watch?v=YONO28H75gY&ab_channel=CodingInformer
How to use MPAndroidCharts in Android applications!
 */

16.
/*https://chatgpt.com
prompt: 'Build a screen that will use the MPAndroiChart to make a pie chart for the getSalesByDate function in the view model
to get the List of sales and fill the pie chart with the data.'
*/


