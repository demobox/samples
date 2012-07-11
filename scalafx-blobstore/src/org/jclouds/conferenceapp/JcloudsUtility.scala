package org.jclouds.conferenceapp

import org.jclouds.ContextBuilder
import java.util.Properties
import org.jclouds.blobstore.BlobStoreContext
import org.jclouds.aws.s3.AWSS3ProviderMetadata
import java.io.InputStream

object JcloudsUtility {
  val awsS3Metadata = AWSS3ProviderMetadata.builder().build();
  val credentials = loadProperties("/jclouds.properties")
  val context = ContextBuilder.newBuilder(awsS3Metadata).overrides(credentials).buildView(classOf[BlobStoreContext])
  
  def loadProperties(classpathResource: String) = {
    val properties = new Properties()
    val resourceStream = getClass().getResourceAsStream(classpathResource)
    try {
        properties.load(resourceStream)
        properties
    } finally {
      resourceStream.close()
    }
  }

  def loadFromCloud(container:String, resource:String) = {
    val blobStore = context.getBlobStore
    val blob = blobStore.getBlob(container, resource)
    blob.getPayload.getInput
  }
    
  def close() {
    context.close()
  }
}
