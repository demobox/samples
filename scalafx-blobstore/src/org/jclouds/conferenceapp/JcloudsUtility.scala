package org.jclouds.conferenceapp

import com.google.common.io.ByteStreams
import org.jclouds.ContextBuilder
import org.jclouds.blobstore.BlobStoreContext
import org.jclouds.aws.s3.AWSS3ProviderMetadata
import java.io.InputStream
import java.util.Properties

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

  def storeInCloud(container:String, resource:String, contentStream: InputStream) = {
    val blobStore = context.getBlobStore
    // BlobBuilder.payload also accepts a stream, but then needs the length
    val content = ByteStreams.toByteArray(contentStream)
    blobStore.putBlob(container, blobStore.blobBuilder(resource).payload(content).build())
  }

  def close() {
    context.close()
  }
}

object ConferenceDataUploader {
  def main(args: Array[String]) {
    val conferenceData = getClass.getResourceAsStream("/conferenceData.zip")
    try {
      JcloudsUtility.storeInCloud("com.steveonjava.conferencedata", "conferenceData.zip", conferenceData)
    } finally {
      conferenceData.close()
      JcloudsUtility.close()
    }
  }
}